package com.example.lexa.services1;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.util.Log;
import android.widget.TextView;

public class ReceiverActivity extends Activity {

    private TextView mTextView;
    private Messenger mServiceMessenger, mMessenger;

    private static final String TAG = "ReceiverActivity";

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
            Log.d(TAG, "onServiceConnected: " + mServiceMessenger);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            mServiceMessenger = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        mTextView = findViewById(R.id.textView);

        mMessenger = new Messenger( new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == RandomGeneratorService.MSG_NEW_DATA) {
                    mTextView.append(msg.obj.toString());
                }
            }
        });

        Intent intent = RandomGeneratorService.newIntent(this);
        intent.putExtra(RandomGeneratorService.PARAM_RECEIVER_MESSENGER, mMessenger);

        bindService(intent, mServiceConnection, Context.BIND_IMPORTANT);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mServiceConnection);
    }

}
