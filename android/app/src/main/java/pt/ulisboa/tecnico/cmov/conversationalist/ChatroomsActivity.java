package pt.ulisboa.tecnico.cmov.conversationalist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;

public class ChatroomsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ChatroomsActivity", "Here");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrooms);
    }

    @Override
    protected void onResume() {
        super.onResume();
        var chatrooms = new ArrayList<>(Data.getJoinedChatrooms().getChatrooms().keySet());
        var adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatrooms);
        ListView chatroomsListView = findViewById(R.id.chatroomsListView);
        chatroomsListView.setOnItemClickListener((parent, view, position, id) -> {
            var chatId = chatrooms.get(position);
            var intent = new Intent(this, PublicChatroomActivity.class);
            intent.putExtra("chatId", chatId);
            startActivity(intent);
        });
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