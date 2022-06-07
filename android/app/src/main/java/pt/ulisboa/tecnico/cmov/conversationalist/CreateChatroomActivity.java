package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

import pt.ulisboa.tecnico.cmov.conversationalist.backend.BackendManager;

public class CreateChatroomActivity extends AppCompatActivity {
    private final List<String> options = Arrays.asList("Public", "Private", "Geo Fenced");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chatroom);
        Spinner spinner = findViewById(R.id.optionsSpinner);
        var adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onCreateChatroom(View view) {
        EditText createChatroomEditText = findViewById(R.id.chatroomIdEditText);
        Spinner spinner = findViewById(R.id.optionsSpinner);
        //int pos = spinner.getSelectedItemPosition();
        BackendManager.createPublicChatroom("Velhinho");
    }
}