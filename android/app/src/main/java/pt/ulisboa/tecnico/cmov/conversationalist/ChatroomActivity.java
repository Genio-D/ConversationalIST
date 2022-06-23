package pt.ulisboa.tecnico.cmov.conversationalist;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;

public class ChatroomActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChatAdapter adapter;
    private final static int LOCATION_REQUEST_CODE = 1001;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.chatId = getIntent().getStringExtra("chatId");
        Log.i("mytag", "Chatroom:" + chatId);
        setContentView(R.layout.activity_chatroom);
        ((TextView) findViewById(R.id.chatIdTextView)).setText(chatId);
        /*create temporary file to store camera photos*/
        Uri tempImagePath = initTempUri();
        registerTakePictureLauncher(tempImagePath);
        Context context = getApplicationContext();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String apiKey = applicationInfo.metaData.getString("com.google.android.geo.API_KEY");
        Places.initialize(getApplicationContext(), apiKey);
        PlacesClient placesClient = Places.createClient(this);
        ImageButton geoBtn = (ImageButton) findViewById(R.id.uploadLocationId);
        geoBtn.setOnClickListener(v -> {
            Intent localizationIntent = new Intent(this, MapsActivity.class);
            localizationIntent.putExtra("getLocation", true);
            startActivityForResult(localizationIntent, LOCATION_REQUEST_CODE);
        });

        ImageButton shareBtn = (ImageButton) findViewById(R.id.shareButton);
        shareBtn.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Yo, make sure to join http://conversationalist.pt/chatId/" + chatId);
            startActivity(shareIntent);
        });

        recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);

        adapter = new ChatAdapter(chatId);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        EditText editText = findViewById(R.id.chatTextId);
        editText.setOnKeyListener((v, keyCode, event) -> {
            try {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(keyCode == KeyEvent.KEYCODE_ENTER) {
                        BackendManager.postMessage(Data.getUsername(), chatId, "text", editText.getText().toString());
                        editText.getText().clear();
                        recyclerView.scrollToPosition(adapter.getItemCount());
                    }
                }
                return false;
            } catch (RuntimeException e) {
                var toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        });

        var br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                var path = intent.getStringExtra("path");
                Log.d("data update", "chatroom: " + path);
                adapter.notifyItemInserted(adapter.getItemCount());
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(br, new IntentFilter("data update"));
    }

    private void registerTakePictureLauncher(Uri tempImagePath) {
        ImageButton imgBtn = (ImageButton) findViewById(R.id.uploadPictureId);
        ActivityResultLauncher<Uri> resultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        try {
                            if(result) {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), tempImagePath);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] b = baos.toByteArray();
                                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                                BackendManager.postMessage(Data.getUsername(), chatId, "image", encodedImage);
                                recyclerView.scrollToPosition(adapter.getItemCount());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        imgBtn.setOnClickListener((v) -> {
            resultLauncher.launch(tempImagePath);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOCATION_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                String locationInfo = data.getStringExtra("location");
                String[] latLong = locationInfo.split(",");
                Log.i("mytag", "latitude is " + latLong[0] + "and longitude is " + latLong[1]);
                // POST MESSAGE HERE
                BackendManager.postMessage(Data.getUsername(), chatId, "text", "https://www.google.com/maps/search/?api=1&query=" + locationInfo);
                recyclerView.scrollToPosition(adapter.getItemCount());
            }
        }
    }

    private Uri initTempUri() {
        File tempImagesDir = new File(getApplicationContext().getFilesDir(), getString(R.string.temp_images_dir));
        tempImagesDir.mkdir();
        File tempImage = new File(tempImagesDir, getString(R.string.temp_image));
        return FileProvider.getUriForFile(getApplicationContext(), getString(R.string.authorities), tempImage);
    }

}