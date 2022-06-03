package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;

public class ChatroomsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrooms);
        var chatrooms = Arrays.asList("0-27", "A1");
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