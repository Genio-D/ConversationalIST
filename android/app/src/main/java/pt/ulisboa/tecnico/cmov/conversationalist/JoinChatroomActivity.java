package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class JoinChatroomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_chatroom);
        var chatrooms = Arrays.asList("1-27", "A4");
        var adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatrooms);
        ListView chatroomsListView = findViewById(R.id.availableChatroomsListView);
        chatroomsListView.setAdapter(adapter);
    }
}