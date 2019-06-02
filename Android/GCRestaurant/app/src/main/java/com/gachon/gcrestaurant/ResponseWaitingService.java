package com.gachon.gcrestaurant;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ResponseWaitingService extends Service {
    public ResponseWaitingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int no = intent.getIntExtra("no",0);
        int time = intent.getIntExtra("time",-1);
        NetworkService.SendMessage(PacketType.RequestWaitingToUser, "no",  String.valueOf(no), "time",  time);

        int noti_id = intent.getIntExtra("no", -1);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(noti_id);

        return START_NOT_STICKY;
    }
}
