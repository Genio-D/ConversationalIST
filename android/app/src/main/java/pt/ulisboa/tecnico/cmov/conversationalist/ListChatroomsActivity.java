package pt.ulisboa.tecnico.cmov.conversationalist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;

public class ListChatroomsActivity extends AppCompatActivity {

    private final static int LOGIN_REQUEST_CODE = 12312;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ChatroomsActivity", "Here");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrooms);
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (!sharedPrefs.contains("username")) {
            Log.i("mytag", "username not in shared preferences, starting login");
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
        else {
            initialize();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            var chatrooms = new ArrayList<>(Data.getJoinedChatrooms().getChatIds());
            var adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatrooms);
            ListView chatroomsListView = findViewById(R.id.chatroomsListView);
            chatroomsListView.setOnItemClickListener((parent, view, position, id) -> {
                var chatId = chatrooms.get(position);
                var intent = new Intent(this, ChatroomActivity.class);
                intent.putExtra("chatId", chatId);
                startActivity(intent);
            });
            chatroomsListView.setAdapter(adapter);
        } catch(Exception e) {
            Log.i("mytag", "no username defined yet probably");
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE) {
            if(resultCode != Activity.RESULT_OK) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
            }
            else {
                initialize();
            }
        }
    }

    private void initialize() {
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String username = sharedPrefs.getString("username", "NOUSERNAMEBRO");
        Log.i("mytag", "username on shared preferences is " + username);
        Data.setUsername(username);
        startService(new Intent(this, UpdateListenerService.class));
        startService(new Intent(this, NotificationService.class));
        Intent previousIntent = getIntent();
        String appLinkAction = previousIntent.getAction();
        Uri appLinkData = previousIntent.getData();
        if(Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            /*user clicked on an app link*/
            String chatIdToJoin = appLinkData.getLastPathSegment();
            Log.i("mytag", "this guy wants to join " + chatIdToJoin);
            BackendManager.joinRoom(Data.getUsername(), chatIdToJoin);
            Data.updateJoinedChatrooms();

            var intent = new Intent(this, ChatroomActivity.class);
            intent.putExtra("chatId", chatIdToJoin);
            startActivity(intent);
        }
    }

    public void onCreateChatroom(View view) {
        var intent = new Intent(this, CreateChatroomActivity.class);
        startActivity(intent);
    }

    public void onJoinChatroom(View view) {
        var intent = new Intent(this, JoinChatroomActivity.class);
        startActivity(intent);
    }
}