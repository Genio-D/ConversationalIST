package pt.ulisboa.tecnico.cmov.conversationalist.backend;

import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import pt.ulisboa.tecnico.cmov.conversationalist.backend.requests.AddUser;
import pt.ulisboa.tecnico.cmov.conversationalist.backend.requests.CreateChatroom;
import pt.ulisboa.tecnico.cmov.conversationalist.backend.responses.JoinedChatrooms;
import pt.ulisboa.tecnico.cmov.conversationalist.backend.responses.PublicChatrooms;

public class BackendManager {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final String url = "http://10.0.2.2:5000";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    // POST REQUESTS
    private static void post(String path, String json) {
        executorService.submit(() -> {
            try {
                var httpClient = new OkHttpClient();
                var body = RequestBody.create(json, JSON);
                var request = new Request.Builder()
                        .url(url + path)
                        .post(body)
                        .addHeader("Accept", "application/json; q=0.5")
                        .build();
                httpClient.newCall(request).execute();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    private static <T> String createRequest(T obj, Class<T> tClass) {
        var moshi = new Moshi.Builder().build();
        var adapter = moshi.adapter(tClass);
        return adapter.toJson(obj);
    }

    public static void createPublicChatroom(String username) {
        var request = createRequest(new CreateChatroom(username), CreateChatroom.class);
        post("/createPublicChatroom", request);
    }

    public static void addUser(String username) {
        var request = createRequest(new AddUser(username), AddUser.class);
        post("/addUser", request);
    }

    // GET REQUESTS
    private static <T> T get(String path, Class<T> returnClass) {
        try {
            var future = new FutureTask<>(() -> {
                var moshi = new Moshi.Builder().build();
                var adapter = moshi.adapter(returnClass);
                var httpClient = new OkHttpClient();
                var request = new Request.Builder().url(url + path).build();
                var response = httpClient.newCall(request).execute();
                return adapter.fromJson(Objects.requireNonNull(response.body()).source());
            });
            executorService.submit(future);
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static PublicChatrooms getPublicChatrooms() {
        return get("/getPublicChatrooms", PublicChatrooms.class);
    }

    public static JoinedChatrooms getJoinedChatrooms() {
        return get("/getJoinedChatrooms", JoinedChatrooms.class);
    }
}
