package com.example.android.concurrency;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.concurrency.constants.Constants;
import com.example.android.concurrency.services.MusicPlayerService;
import com.example.android.concurrency.services.MyDownloadService;
import com.example.android.concurrency.services.MyForegroundService;
import com.example.android.concurrency.services.MyIntentService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    public static final String MESSAGE_KEY = "message_key";
    private ScrollView mScroll;
    private TextView mLog;
    private Button mPlayButton;
    private ProgressBar mProgressBar;
    private MusicPlayerService mMusicPlayerService;
    private boolean mBound=false;
    private ServiceConnection mServiceCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {

            MusicPlayerService.MyServiceBinder myServiceBinder=
                    (MusicPlayerService.MyServiceBinder) iBinder;
            mMusicPlayerService=myServiceBinder.getService();
            mBound=true;
            Log.d(TAG, "onServiceConnected");

            if(mMusicPlayerService.isPlaying()){
                mPlayButton.setText("Pause");
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            mBound=false;
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String songName=intent.getStringExtra(MESSAGE_KEY);
            String result=intent.getStringExtra(MESSAGE_KEY);
            if(result == "done")
                mPlayButton.setText("Play");

            //log(songName+" Downloaded...");

            Log.d(TAG, "onReceive: Thread name: "+Thread.currentThread().getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    public void onBtnMusicClicked(View view) {

        if(mBound){

            if(mMusicPlayerService.isPlaying()){
                mMusicPlayerService.pause();
                mPlayButton.setText("Play");
            }else{
                Intent intent=new Intent(MainActivity.this,MusicPlayerService.class);
                intent.setAction(Constants.MUSIC_SERVICE_ACTION_START);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                }else{
                    startService(intent);
                }

                mMusicPlayerService.play();
                mPlayButton.setText("Pause");
            }

        }

    }

    public void runCode(View v) {
        log("Playing Music Buddy!");
        displayProgressBar(true);

        //send intent to download service

        for (String song:Playlist.songs){
            Intent intent=new Intent(MainActivity.this,MyIntentService.class);
            intent.putExtra(MESSAGE_KEY,song);

            startService(intent);
        }

//        Intent intent=new Intent(MainActivity.this,MyForegroundService.class);
//        startService(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
        Intent intent=new Intent(MainActivity.this,MusicPlayerService.class);
        bindService(intent,mServiceCon,Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mReceiver,new IntentFilter(MusicPlayerService.MUSIC_COMPLETE));

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound){
            unbindService(mServiceCon);
            mBound=false;
        }

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mReceiver);

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
        mPlayButton=findViewById(R.id.btnPlayMusic);
    }
}