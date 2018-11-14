package com.example.android.concurrency;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class App extends Application {


    public static final String CHANNEL_ID="channel_id";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        NotificationChannel channel=new NotificationChannel(
                CHANNEL_ID,
                "Foregound Services Notifications",
                NotificationManager.IMPORTANCE_LOW
        );

        channel.setDescription("This channel is for showing notifications of foreground services");

        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

    }
}
