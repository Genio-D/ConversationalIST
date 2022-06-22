package pt.ulisboa.tecnico.cmov.conversationalist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;

public class NotificationService extends Service {
    private static int notificationId = 0;
    private static BroadcastReceiver br;

    public void unregisterBr() {
        if (br != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
        }
    }

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        new Thread(this::awaitNewMessage).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var importance = NotificationManager.IMPORTANCE_DEFAULT;
            var channel = new NotificationChannel("notifications", "notifications", importance);
            channel.setDescription("notifications");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void awaitNewMessage() {
        unregisterBr();
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                var path = intent.getStringExtra("path");
                var message = Data.getMessageCache().getMessage(path);
                var clickIntent = new Intent(context, ChatroomActivity.class);
                var chatId = Data.getChatIdFromPath(path);
                clickIntent.putExtra("chatId", chatId);
                Log.d("data update", "Notification: " + chatId);
//                clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                var builder= new NotificationCompat.Builder(context, "notifications")
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle(message.getAuthor() + " in " + chatId)
                        .setContentText(message.getType() == ChatMessage.TEXT ? message.getContent() : "Posted an image")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setTimeoutAfter(60000)
                        .setContentIntent(PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_IMMUTABLE));
                var notificationManager = NotificationManagerCompat.from(context);
                notificationId +=  1;
                notificationManager.notify(notificationId, builder.build());
            }
        };
        var intentFilter = new IntentFilter("data update");
        LocalBroadcastManager.getInstance(this).registerReceiver(br, intentFilter);
    }
}