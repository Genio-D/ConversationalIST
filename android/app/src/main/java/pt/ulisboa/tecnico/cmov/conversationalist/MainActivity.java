package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static boolean validUsername(String username) {
        return Data.onlyAlphanum(username);
    }

    public void onRegister(View v) {
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        var username = usernameEditText.getText().toString();
        try {
            if (validUsername(username)) {
                Data.registerUsername(username);
                Intent previousActivityIntent = getIntent();
                Log.i("mytag", "got a login intent");
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.apply();
                Log.i("mytag", "wrote " + username + " to shared preferences");
                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();
            } else {
                throw new RuntimeException("invalid username");
            }
        } catch (RuntimeException e) {
            var toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onLogin(View v) {
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        var username = usernameEditText.getText().toString();
        try {
            if (validUsername(username)) {
                Data.setUsername(username);
                Data.getJoinedChatrooms();
            } else {
                throw new RuntimeException("invalid username");
            }
        } catch (RuntimeException e) {
            var toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}