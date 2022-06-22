package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;

public class JoinChatroomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_chatroom);
        var chatrooms = new ArrayList<>(BackendManager.getPublicChatrooms().getList());
        var adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatrooms);
        ListView chatroomsListView = findViewById(R.id.availableChatroomsListView);
        chatroomsListView.setOnItemClickListener((parent, view, position, id) -> {
            var chatId = chatrooms.get(position);
            Log.i("mytag", "joining " + chatId);
            BackendManager.joinRoom(Data.getUsername(), chatId);
            Data.updateJoinedChatrooms();
            var intent = new Intent(this, ChatroomActivity.class);
            intent.putExtra("chatId", chatId);
            startActivity(intent);
        });
        chatroomsListView.setAdapter(adapter);
    }
}