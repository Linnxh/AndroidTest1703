package com.test.lxh.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.test.lxh.R;
import com.test.lxh.service.AppService;

/**
 * Created by LXH on 17/4/5.
 */

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.btn_listenScrollView).setOnClickListener(this);
        findViewById(R.id.btn_album).setOnClickListener(this);

        startService();
    }

    private boolean isServiceRunning;
    private ServiceConnection serviceConnection;

    private void startService() {
        if (null == serviceConnection) {
            //开启服务
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.i("AAAA", "AppService disconnected!!!!!!!!!!!!!!!");
                }

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    isServiceRunning = true;
                    Log.i("AAAA", "AppService connected!!!!!!!!!!!!!!!");
                }
            };

            Intent intentService = new Intent(MainActivity2.this, AppService.class);
            bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_listenScrollView:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn_album:
                startActivity(new Intent(this, AlbumActivity.class));
                break;
        }
    }

}
