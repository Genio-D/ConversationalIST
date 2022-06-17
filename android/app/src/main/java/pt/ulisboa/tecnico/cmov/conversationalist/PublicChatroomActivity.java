package pt.ulisboa.tecnico.cmov.conversationalist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;

public class PublicChatroomActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_chatroom);
        ImageButton imgBtn = (ImageButton) findViewById(R.id.publicUploadPictureId);
        Uri tempImagePath = initTempUri();
        Log.i("mytag", "created image path : " + tempImagePath.toString());
        registerTakePictureLauncher(tempImagePath);

        recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);

        var chatId = getIntent().getStringExtra("chatId");
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

    private void registerTakePictureLauncher(Uri tempImagePath) {
        ImageButton imgBtn = (ImageButton) findViewById(R.id.publicUploadPictureId);
        ActivityResultLauncher<Uri> resultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        Log.i("mytag", "GREAT success");
                        ImageView imageView = (ImageView) findViewById(R.id.imageView4);
                        imageView.setImageURI(null);
                        imageView.setImageURI(tempImagePath);
                    }
                });
        imgBtn.setOnClickListener((v) -> {
            resultLauncher.launch(tempImagePath);
            Log.i("mytag", "great success");
        });

    }


    private Uri initTempUri() {
        File tempImagesDir = new File(getApplicationContext().getFilesDir(), getString(R.string.temp_images_dir));
        tempImagesDir.mkdir();
        File tempImage = new File(tempImagesDir, getString(R.string.temp_image));
        return FileProvider.getUriForFile(getApplicationContext(), getString(R.string.authorities), tempImage);
    }

}