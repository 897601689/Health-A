package com.health_a.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.health_a.activity.LoginActivity;

/**
 * Created by Admin on 2016/11/22.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent mainActivityIntent = new Intent(context, LoginActivity.class);  // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }
}
