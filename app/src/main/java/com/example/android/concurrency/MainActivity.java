package com.example.android.concurrency;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.concurrency.services.MusicPlayerService;
import com.example.android.concurrency.services.MyDownloadService;
import com.example.android.concurrency.services.MyIntentService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    public static final String MESSAGE_KEY = "message_key";
    private ScrollView mScroll;
    private TextView mLog;
    private ProgressBar mProgressBar;
    private MusicPlayerService mMusicService;
    private final ServiceConnection mServiceCon=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            MusicPlayerService.MyServiceBinder serviceBinder=
                    (MusicPlayerService.MyServiceBinder) iBinder;

            mMusicService=serviceBinder.getService();
            Log.d(TAG, "onServiceConnected: Service connected");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (mMusicService != null) {
                mMusicService=null;
            }
            Log.d(TAG, "onServiceDisconnected: Service Disconnected");

        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String songName=intent.getStringExtra(MESSAGE_KEY);
            log(songName+" Downloaded...");

            Log.d(TAG, "onReceive: Thread name: "+Thread.currentThread().getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    public void runCode(View v) {
        log(mMusicService.getValue());
        displayProgressBar(true);

        //send intent to download service

//        for (String song:Playlist.songs){
//            Intent intent=new Intent(MainActivity.this,MyIntentService.class);
//            intent.putExtra(MESSAGE_KEY,song);
//
//            startService(intent);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(MainActivity.this,MusicPlayerService.class);
        bindService(intent,mServiceCon,Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mServiceCon);
        Log.d(TAG, "onStop: Service Unbind");
    }

    public void clearOutput(View v) {

        Intent intent=new Intent(MainActivity.this,MyDownloadService.class);
        stopService(intent);

        mLog.setText("");
        scrollTextToEnd();
    }

    public void log(String message) {
        Log.i(TAG, message);
        mLog.append(message + "\n");
        scrollTextToEnd();
    }

    private void scrollTextToEnd() {
        mScroll.post(new Runnable() {
            @Override
            public void run() {
                mScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void displayProgressBar(boolean display) {
        if (display) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void initViews() {
        mScroll = (ScrollView) findViewById(R.id.scrollLog);
        mLog = (TextView) findViewById(R.id.tvLog);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

}