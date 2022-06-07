package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.conversationalist.backend.BackendManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAccept(View view) {
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        var username = usernameEditText.getText().toString();

        BackendManager.addUser(username);
        var intent = new Intent(this, ChatroomsActivity.class);
        startActivity(intent);
    }
}