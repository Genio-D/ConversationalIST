package pt.ulisboa.tecnico.cmov.conversationalist.chatrooms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.net.ServerSocket;
import java.util.List;

import pt.ulisboa.tecnico.cmov.conversationalist.createchatroom.CreateChatroomActivity;
import pt.ulisboa.tecnico.cmov.conversationalist.JoinChatroomActivity;
import pt.ulisboa.tecnico.cmov.conversationalist.R;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;

public class ChatroomsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrooms);

        var model = new ViewModelProvider(this).get(ChatroomsViewModel.class);
        var chatrooms = model.getJoinedChatrooms().getValue();
        var adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatrooms);
        ListView chatroomsListView = findViewById(R.id.chatroomsListView);
        chatroomsListView.setAdapter(adapter);

        var observer = new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> s) {
                adapter.notifyDataSetChanged();
            }
        };
        model.getJoinedChatrooms().observe(this, observer);
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