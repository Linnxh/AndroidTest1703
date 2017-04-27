package com.test.lxh.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.test.lxh.App;
import com.test.lxh.AppAlarmReceiver;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;


/**
 * 轮询服务
 */
public class AppService extends Service {


    private Context mContext;
    private AppBinder myBinder = new AppBinder();

    // Notification
    private NotificationManager mNotificationManager;
    private int notificationID = 100;
    public int totalMessages = 0;

    public class AppBinder extends Binder {
        public AppService getService() {
            return AppService.this;
        }
    }

    @Override
    public void onCreate() {
        mContext = this;
        super.onCreate();
//        longPull(AppLongPull.getInstance());
        new SubThread().start();
    }

    @Override
    public IBinder onBind(Intent intent) {
//        AppLongPull.getInstance().appService = this;
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        AppLongPull.getInstance().appService = null;
//        stopLongPull(AppLongPull.getInstance());
        return true;
    }


    private boolean isRunning;
    private static final long SLEEP_LONGPULL = 5000;

    private class SubThread extends Thread {
        @Override
        public void run() {
            super.run();
            if (!isRunning)
                isRunning = true;
            while (isRunning) {
                try {
                    OkHttpUtils
                            .post()
                            .url("http://test.lxh.getcommissiongrpbydate")
                            .addParams("username", "hyman")
                            .addParams("password", "123")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.i("AAAA", "res-->" + response.toString());
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        //根据返回值，判断相应的状态，可以注册一个receiver，接收数据后进行相应操作
                                        if (object.optInt("code") == 200) {
                                            Intent intent=new Intent(AppAlarmReceiver.REVEIVER_ACTION);
                                            mContext.sendBroadcast(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    Thread.sleep(SLEEP_LONGPULL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}