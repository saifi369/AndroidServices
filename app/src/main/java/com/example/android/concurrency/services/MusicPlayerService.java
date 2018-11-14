package com.example.android.concurrency.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.android.concurrency.MainActivity;
import com.example.android.concurrency.R;
import com.example.android.concurrency.constants.Constants;

import java.net.Inet4Address;

import static com.example.android.concurrency.App.CHANNEL_ID;

public class MusicPlayerService extends Service {

    private static final String TAG = "MyTag";
    public static final String MUSIC_COMPLETE = "MusicComplete";
    private final Binder mBinder = new MyServiceBinder();
    private MediaPlayer mPlayer;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        mPlayer=MediaPlayer.create(this,R.raw.youngasthemorning);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent=new Intent(MUSIC_COMPLETE);
                intent.putExtra(MainActivity.MESSAGE_KEY,"done");
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(intent);
                stopForeground(true);
                stopSelf();

            }
        });
    }


    public class MyServiceBinder extends Binder{
        public MusicPlayerService getService(){
            return  MusicPlayerService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (intent.getAction()){

            case Constants.MUSIC_SERVICE_ACTION_PLAY:{
                Log.d(TAG, "onStartCommand: play called");
                play();
                break;
            }
            case Constants.MUSIC_SERVICE_ACTION_PAUSE:{
                Log.d(TAG, "onStartCommand: pause called");
                pause();
                break;
            }
            case Constants.MUSIC_SERVICE_ACTION_STOP:{
                Log.d(TAG, "onStartCommand: stop called");
                stopForeground(true);
                stopSelf();
                break;
            }
            case Constants.MUSIC_SERVICE_ACTION_START:{
                Log.d(TAG, "onStartCommand: start called");
                showNotification();
                break;
            }
            default:{

            }
        }

        Log.d(TAG, "onStartCommand: ");
        return START_NOT_STICKY;
    }

    private void showNotification() {

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID);

        //Intent for play button
        Intent pIntent=new Intent(this,MusicPlayerService.class);
        pIntent.setAction(Constants.MUSIC_SERVICE_ACTION_PLAY);

        PendingIntent playIntent=PendingIntent.getService(this,100,pIntent,0);

        //Intent for pause button
        Intent psIntent=new Intent(this,MusicPlayerService.class);
        psIntent.setAction(Constants.MUSIC_SERVICE_ACTION_PAUSE);

        PendingIntent pauseIntent=PendingIntent.getService(this,100,psIntent,0);

        //Intent for stop button
        Intent sIntent=new Intent(this,MusicPlayerService.class);
        sIntent.setAction(Constants.MUSIC_SERVICE_ACTION_STOP);

        PendingIntent stopIntent=PendingIntent.getService(this,100,sIntent,0);

        builder.setContentTitle("U4Universe Music Player")
                .setContentText("This is demo music player")
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,"Play",playIntent))
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,"Pause",pauseIntent))
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,"Stop",stopIntent));


        startForeground(123,builder.build());

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind: ");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        mPlayer.release();
    }

    //public client methods

    public boolean isPlaying(){
        return mPlayer.isPlaying();
    }

    public void play(){
        mPlayer.start();
    }

    public void pause(){
        mPlayer.pause();
    }


}
