package com.test.lxh;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.test.lxh.service.AppService;

/**
 * Created by LXH on 17/4/26.
 * 接收轮询开启的广播
 */

public class AppAlarmReceiver extends WakefulBroadcastReceiver {

    public static String REVEIVER_ACTION = "com.lxh.myreceiver";

    public static int i = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent service = new Intent(context, AppService.class);
//        // 启动 service 并保持设备唤醒状态直到调用 completeWakefulIntent()
//        startWakefulService(context, service);
//        Toast.makeText(context, "收到广播" + (++i) + "系统时间-->" + System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
        // i值会出错，当进程长时间置于后台i值会发生变化
        Log.i("AAAA", "收到广播" + (++i) + "系统时间-->" + System.currentTimeMillis());
    }
}
