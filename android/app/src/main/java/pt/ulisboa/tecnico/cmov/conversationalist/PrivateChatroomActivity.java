package pt.ulisboa.tecnico.cmov.conversationalist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;

public class PrivateChatroomActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var chatId = getIntent().getStringExtra("chatId");
        setContentView(R.layout.activity_private_chatroom);
        ((TextView) findViewById(R.id.privateChatIdTextView)).setText(chatId);
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (!sharedPrefs.contains("username")) {
            Log.i("mytag", "username not in shared preferences, starting login");
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("login", true);
            startActivityForResult(intent, 1);
        } else {
            Intent previousIntent = getIntent();
            String appLinkAction = previousIntent.getAction();
            Uri appLinkData = previousIntent.getData();
            if(Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
                /*user clicked on an app link*/
                String chatIdToJoin = appLinkData.getLastPathSegment();
                Log.i("mytag", "this guy wants to join " + chatIdToJoin);
            }
            String username = sharedPrefs.getString("username", "NOUSERNAME");
            /*create temporary file to store camera photos*/
            Uri tempImagePath = initTempUri();
            registerTakePictureLauncher(tempImagePath, chatId);
            ImageButton shareBtn = (ImageButton) findViewById(R.id.shareButton);
            shareBtn.setOnClickListener(v -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Yo, make sure to join http://conversationalist.pt/chatId/" + chatId);
                startActivity(shareIntent);
            });
            recyclerView = (RecyclerView) findViewById(R.id.privateChatRecyclerView);
            this.adapter = new ChatAdapter(chatId);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.privateChatRecyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    ActivityResultLauncher<Intent> startForLoginResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i("mytag", "returned from login activity");
                    if(result.getResultCode() != Activity.RESULT_OK) {
                        Log.i("mytag", "result was not ok, exiting");
                        finish();
                    }
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                var chatId = getIntent().getStringExtra("chatId");
                SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                Log.i("mytag", "got result from activity");
                Intent previousIntent = getIntent();
                String appLinkAction = previousIntent.getAction();
                Uri appLinkData = previousIntent.getData();
                if(Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
                    /*user clicked on an app link*/
                    String chatIdToJoin = appLinkData.getLastPathSegment();
                    Log.i("mytag", "this guy wants to join " + chatIdToJoin);
                }
                String username = sharedPrefs.getString("username", "NOUSERNAME");
                /*create temporary file to store camera photos*/
                Uri tempImagePath = initTempUri();
                registerTakePictureLauncher(tempImagePath, chatId);
                ImageButton shareBtn = (ImageButton) findViewById(R.id.shareButton);
                shareBtn.setOnClickListener(v -> {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Yo, make sure to join http://conversationalist.pt/chatId/" + chatId);
                    startActivity(shareIntent);
                });
                recyclerView = (RecyclerView) findViewById(R.id.privateChatRecyclerView);
                this.adapter = new ChatAdapter(chatId);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.privateChatRecyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
            }
            else {
                Log.i("mytag", "result was not ok, exiting");
                finish();
            }
        }
    }

    private void registerTakePictureLauncher(Uri tempImagePath, String chatId) {
        ImageButton imgBtn = (ImageButton) findViewById(R.id.publicUploadPictureId);
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


    private Uri initTempUri() {
        File tempImagesDir = new File(getApplicationContext().getFilesDir(), getString(R.string.temp_images_dir));
        tempImagesDir.mkdir();
        File tempImage = new File(tempImagesDir, getString(R.string.temp_image));
        return FileProvider.getUriForFile(getApplicationContext(), getString(R.string.authorities), tempImage);
    }

}