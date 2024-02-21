package com.tencent.ugsv_flutter.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.tencent.ugsv_flutter.config.TCConfigManager;

/**
 * 用于接收系统改变系统语言的广播
 */
public class LanguageChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TCConfigManager.setSystemLocal(context.getResources().getConfiguration().locale);
    }
}
