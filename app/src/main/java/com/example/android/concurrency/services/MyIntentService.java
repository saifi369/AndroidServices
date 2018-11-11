package com.example.android.concurrency.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.android.concurrency.MainActivity;

public class MyIntentService extends IntentService {

    private static final String TAG = "MyTag";

    public MyIntentService() {
        super("MyIntentService");
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: MyIntentService");

        String songName=intent.getStringExtra(MainActivity.MESSAGE_KEY);
        downloadSong(songName);

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
        Log.d(TAG, "onDestroy: MyIntentService");
    }
}
