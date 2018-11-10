package com.example.android.concurrency.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.android.concurrency.DownloadHandler;
import com.example.android.concurrency.DownloadThread;
import com.example.android.concurrency.MainActivity;

public class MyDownloadService extends Service {
    private static final String TAG = "MyTag";
    private DownloadThread mDownlaodThread;

    //this is started service

    public MyDownloadService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: called");

        mDownlaodThread=new DownloadThread();
        mDownlaodThread.start();

        while (mDownlaodThread.mHandler == null){

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: called");
        final String songName=intent.getStringExtra(MainActivity.MESSAGE_KEY);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                downloadSong(songName);
            }
        });

        thread.start();

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: called");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
    }
}
