package com.example.android.concurrency;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.concurrency.services.MyDownloadService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    public static final String MESSAGE_KEY = "message_key";
    private ScrollView mScroll;
    private TextView mLog;
    private ProgressBar mProgressBar;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        mHandler=new Handler();
    }

    public void runCode(View v) {
        log("Running code");
        displayProgressBar(true);

        //send intent to download service

        ResultReceiver resultReceiver=new MyDownlaodResultReceiver(null);

        for (String song:Playlist.songs){
            Intent intent=new Intent(MainActivity.this,MyDownloadService.class);
            intent.putExtra(MESSAGE_KEY,song);
            intent.putExtra(Intent.EXTRA_RESULT_RECEIVER,resultReceiver);

            startService(intent);
        }

    }

    private void initViews() {
        mScroll = (ScrollView) findViewById(R.id.scrollLog);
        mLog = (TextView) findViewById(R.id.tvLog);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
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

    public class MyDownlaodResultReceiver extends ResultReceiver{

        public MyDownlaodResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if(resultCode == RESULT_OK && resultData!=null){

                Log.d(TAG, "onReceiveResult: Thread name: "+Thread.currentThread().getName());

                final String songName=resultData.getString(MESSAGE_KEY);

//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        log(songName+" Downloaded");
//                    }
//                });

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        log(songName+" Downloaded");
                    }
                });

            }

        }
    }
}