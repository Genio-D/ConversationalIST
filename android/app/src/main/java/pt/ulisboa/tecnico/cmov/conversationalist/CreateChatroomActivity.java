package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import pt.ulisboa.tecnico.cmov.conversationalist.R;
import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;

public class CreateChatroomActivity extends AppCompatActivity {
    private final List<String> options = Arrays.asList("Public", "Private", "Geo Fenced");
    private final static int PICK_LOCATION_REQUEST_CODE = 4242;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chatroom);
        Spinner spinner = findViewById(R.id.optionsSpinner);
        var adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        EditText radiusView = (EditText) findViewById(R.id.pickRadius);
        Button pickBtn = (Button) findViewById(R.id.pickLocationButton);
        pickBtn.setOnClickListener(v -> {
            Intent localizationIntent = new Intent(this, MapsActivity.class);
            localizationIntent.putExtra("getLocation", true);
            startActivityForResult(localizationIntent, PICK_LOCATION_REQUEST_CODE);
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 2) {
                    pickBtn.setVisibility(View.VISIBLE);
                    radiusView.setVisibility(View.VISIBLE);
                }
                else {
                    pickBtn.setVisibility(View.INVISIBLE);
                    radiusView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private static boolean validChatId(String chatId) {
        return Data.onlyAlphanum(chatId);
    }

    public void onCreateChatroom(View view) {
        EditText createChatroomEditText = findViewById(R.id.chatroomIdEditText);
        var chatId = createChatroomEditText.getText().toString();
        try {
            if (validChatId(chatId)) {
                Spinner spinner = findViewById(R.id.optionsSpinner);
                int pos = spinner.getSelectedItemPosition();
                switch (pos) {
                    case 0:
                        BackendManager.createPublicChatroom(Data.getUsername(), chatId);
                        Data.updateJoinedChatrooms();
                        finish();
                        break;
                    case 1:
                        break;
                    case 2:
                        String locationInfo = ((TextView) findViewById(R.id.showCoords)).getText().toString();
                        String radius = ((EditText) findViewById(R.id.pickRadius)).getText().toString();
                        if(!Objects.equals(radius, "") && !locationInfo.equals("")) {
                            String[] latLong = locationInfo.split(",");
                            double lat = Double.parseDouble(latLong[0]);
                            double lng = Double.parseDouble(latLong[1]);
                            double rad = Double.parseDouble(radius);
                            BackendManager.createGeoChatroom(Data.getUsername(), chatId, lat, lng, rad);
                            Data.updateJoinedChatrooms();
                            finish();
                        }
                }
            } else {
                throw new RuntimeException("invalid chat id");
            }
        } catch (RuntimeException e) {
            var toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_LOCATION_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                String locationInfo = data.getStringExtra("location");
                Log.i("mytag", "coords are " + locationInfo);
                ((TextView) findViewById(R.id.showCoords)).setText(locationInfo);
            }
        }
    }
}