package pt.ulisboa.tecnico.cmov.conversationalist.data.backend;

import com.squareup.moshi.Moshi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests.AddUser;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests.CreateGeoChatroom;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests.CreatePublicChatroom;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests.JoinRoom;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests.PostMessage;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.JoinedChatrooms;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.Message;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.PublicChatrooms;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.Response;

public class BackendManager {
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final String url = "http://10.0.2.2:5000";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static <T> T sendRequest(Request request, Class<T> responseClass) throws IOException {
        var moshi = new Moshi.Builder().build();
        var adapter = moshi.adapter(responseClass);
        var httpClient = new OkHttpClient();
        var response = httpClient.newCall(request).execute();
        return adapter.fromJson(Objects.requireNonNull(response.body()).source());
    }

    private static <T> T submitTask(Callable<T> callable) {
        try {
            var future = new FutureTask<T>(callable);
            executorService.submit(future);
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static <T extends Response> T get(String path,Class<T> responseClass) {
        var request = new Request.Builder().url(url + path).build();
        var response = submitTask(() -> sendRequest(request, responseClass));
        if (response.isError()) {
            throw new RuntimeException(response.getErrorMessage());
        } else {
            return response;
        }
    }

    private static void post(String path, String json) {
        var body = RequestBody.create(json, JSON);
        var request = new Request.Builder()
                .url(url + path)
                .post(body)
                .addHeader("Accept", "application/json; q=0.5")
                .build();
        var response = submitTask(() -> sendRequest(request, Response.class));
        if (response.isError()) {
            throw new RuntimeException(response.getErrorMessage());
        }
    }

    private static <T> String createRequest(T obj, Class<T> tClass) {
        var moshi = new Moshi.Builder().build();
        var adapter = moshi.adapter(tClass);
        return adapter.toJson(obj);
    }

    public static void addUser(String username) {
        var request = createRequest(new AddUser(username), AddUser.class);
        post("/addUser", request);
    }

    public static void postMessage(String username, String chatId, String messageType, String content) {
        var request = createRequest(new PostMessage(username, chatId, messageType, content), PostMessage.class);
        post("/postMessage", request);
    }

    public static void createPublicChatroom(String username, String chatId) {
        var request = createRequest(new CreatePublicChatroom(username, chatId), CreatePublicChatroom.class);
        post("/createPublicChatroom", request);
    }

    public static void createGeoChatroom(String username,
                                         String chatId,
                                         double latitude,
                                         double longitude,
                                         double radius) {
        var request = createRequest(new CreateGeoChatroom(username, chatId, latitude, longitude, radius), CreateGeoChatroom.class);
        post("/createGeoChatroom", request);
    }

    public static PublicChatrooms getPublicChatrooms() {
        return get("/getPublicChatrooms", PublicChatrooms.class);
    }

    public static JoinedChatrooms getJoinedChatrooms(String username) {
        return get("/getJoinedChatrooms/" + username, JoinedChatrooms.class);
    }

    public static Message getMessage(String path) {
        return get( "/chatrooms/" + path, Message.class);
    }

    public static void joinRoom(String username, String chatId) {
        var request = createRequest(new JoinRoom(chatId, username), JoinRoom.class);
        post("/joinRoom", request);
    }
}
