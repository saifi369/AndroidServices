package com.example.android.concurrency.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.android.concurrency.MainActivity;

public class MyDownloadService extends Service {
    private static final String TAG = "MyTag";

    //this is started service

    public MyDownloadService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String songName=intent.getStringExtra(MainActivity.MESSAGE_KEY);
        downloadSong(songName);

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void downloadSong(final String songName){
        Log.d(TAG, "run: staring download");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "downloadSong: "+songName+" Downloaded...");
    }

}
