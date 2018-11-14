package com.example.android.concurrency.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.concurrency.R;

import static com.example.android.concurrency.App.CHANNEL_ID;

public class MyOreoService extends Service {

    public static final String TAG = "MyTag";

    public MyOreoService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Called");

        startForeground(1001,getNotification());

        return START_STICKY;
    }

    private Notification getNotification() {

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID);

        builder.setContentTitle("Foreground Service Notification")
                .setContentText("This is example notification")
                .setSmallIcon(R.mipmap.ic_launcher);

        return builder.build();


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Called");
    }
}
