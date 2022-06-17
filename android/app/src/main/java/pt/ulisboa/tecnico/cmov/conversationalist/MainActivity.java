package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
        Button privChatBtn = (Button) findViewById(R.id.privateChatroomButton);
        privChatBtn.setOnClickListener(v -> {
            var intent = new Intent(this, PrivateChatroomActivity.class);
            intent.putExtra("chatId", "idfromthelistview");
            startActivity(intent);
        });
    }

    public void onAccept(View v) {
        Intent previousActivityIntent = getIntent();
        if(previousActivityIntent.getBooleanExtra("login", false)) {
            Log.i("mytag", "got a login intent");
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", "user1");
            editor.apply();
            Log.i("mytag", "wrote user1 to shared preferences");
            Intent data = new Intent();
            setResult(RESULT_OK, data);
            MainActivity.this.finish();
        }
        else {
            var intent = new Intent(this, ChatroomsActivity.class);
            startActivity(intent);
        }
    }
}