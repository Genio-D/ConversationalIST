package pt.ulisboa.tecnico.cmov.conversationalist;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(this::awaitNewMessage).start();
        return super.onStartCommand(intent, flags, startId);
    }

    public void awaitNewMessage() {

    }
}