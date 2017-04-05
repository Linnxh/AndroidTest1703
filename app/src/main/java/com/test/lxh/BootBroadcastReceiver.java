package com.test.lxh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.test.lxh.activity.MainActivity;

/**
 * Created by LXH on 17/3/30.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            final Intent mainActivityIntent = new Intent(context, MainActivity.class);  // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            final Context mContext = context;
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    mContext.startActivity(mainActivityIntent);
                }
            }, 10000);

        }
    }
}
