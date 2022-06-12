package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import pt.ulisboa.tecnico.cmov.conversationalist.chatrooms.ChatroomsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAccept(View v) {
        var intent = new Intent(this, ChatroomsActivity.class);
        startActivity(intent);
    }
}