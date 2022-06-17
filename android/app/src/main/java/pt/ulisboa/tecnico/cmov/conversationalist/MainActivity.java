package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRegister(View v) {
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        var username = usernameEditText.getText().toString();
        Data.registerUsername(username);
        startService(new Intent(this, UpdateListenerService.class));
        startActivity(new Intent(this, ChatroomsActivity.class));
    }

    public void onLogin(View v) {
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        var username = usernameEditText.getText().toString();
        Data.setUsername(username);
        startService(new Intent(this, UpdateListenerService.class));
        startActivity(new Intent(this, ChatroomsActivity.class));
    }
}