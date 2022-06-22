package pt.ulisboa.tecnico.cmov.conversationalist;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;

public class UpdateListenerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(this::listen).start();
        return super.onStartCommand(intent, flags, startId);
    }

    public void listen() {
        try {
            var socket = new Socket("10.0.2.2", 5001);
            var printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(Data.getUsername());
            printWriter.flush();

            while (true) {
                var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                var line = reader.readLine();
                if (line != null) {
                    Log.d("UpdateListener", line);
                    Data.getMessageCache().getMessage(line);
                    Data.updateJoinedChatrooms();
                    var intent = new Intent("data update");
                    intent.putExtra("path", line);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}