package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pt.ulisboa.tecnico.cmov.conversationalist.chatrooms.ChatroomsActivity;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button pubChatBtn = (Button) findViewById(R.id.publicChatroomButton);
        pubChatBtn.setOnClickListener(v -> {
            var intent = new Intent(this, PublicChatroomActivity.class);
            startActivity(intent);
        });
    }

    public void onAccept(View v) {
        var intent = new Intent(this, ChatroomsActivity.class);
        startActivity(intent);
    }
}