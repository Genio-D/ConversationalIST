package pt.ulisboa.tecnico.cmov.conversationalist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;

public class PublicChatroomActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var chatId = getIntent().getStringExtra("chatId");

        setContentView(R.layout.activity_public_chatroom);
        Uri tempImagePath = initTempUri();
        registerTakePictureLauncher(tempImagePath, chatId);

        recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);

        this.adapter = new ChatAdapter(chatId);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        EditText editText = findViewById(R.id.publicChatTextId);
        editText.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    BackendManager.postMessage(Data.getUsername(), chatId, "text", editText.getText().toString());
                }
            }
            return false;
        });

        var br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                var path = intent.getStringExtra("path");
                Log.d("Broadcast receiver", "new message: " + path);
                Data.getMessageCache().getMessage(path);
                adapter.notifyItemInserted(adapter.getItemCount() - 1);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(br, new IntentFilter("new message"));
    }

    private void registerTakePictureLauncher(Uri tempImagePath, String chatId) {
        ImageButton imgBtn = (ImageButton) findViewById(R.id.publicUploadPictureId);
        ActivityResultLauncher<Uri> resultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), tempImagePath);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] b = baos.toByteArray();
                            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            BackendManager.postMessage(Data.getUsername(), chatId, "image", encodedImage);
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