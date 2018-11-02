package com.example.android.concurrency;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class DownloadThread extends Thread {

    private static final String TAG = "MyTag";
    private final MainActivity mActivity;
    public DownloadHandler mHandler;

    public DownloadThread(MainActivity activity) {
        this.mActivity=activity;
    }

    @Override
    public void run() {

        Looper.prepare();
        mHandler=new DownloadHandler(mActivity);
        Looper.loop();

    }
}
