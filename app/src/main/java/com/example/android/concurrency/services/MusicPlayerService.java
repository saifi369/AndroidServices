package com.example.android.concurrency.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
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

import java.net.Inet4Address;

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
        Log.d(TAG, "onStartCommand: ");
        return START_NOT_STICKY;
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
