package pt.ulisboa.tecnico.cmov.conversationalist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import pt.ulisboa.tecnico.cmov.conversationalist.backend.BackendManager;

public class ChatroomsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrooms);
        var chatrooms = BackendManager.getPublicChatrooms().getList();
        var adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatrooms);
        ListView chatroomsListView = findViewById(R.id.chatroomsListView);
        chatroomsListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_chatrooms);
        var chatrooms = BackendManager.getPublicChatrooms().getList();
        var adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatrooms);
        ListView chatroomsListView = findViewById(R.id.chatroomsListView);
        chatroomsListView.setAdapter(adapter);
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