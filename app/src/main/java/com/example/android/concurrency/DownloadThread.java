package com.example.android.concurrency;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class DownloadThread extends Thread {

    private static final String TAG = "MyTag";
    public DownloadHandler mHandler;

    public DownloadThread() {
    }

    @Override
    public void run() {

        Looper.prepare();
        mHandler=new DownloadHandler();
        Looper.loop();

    }
}
