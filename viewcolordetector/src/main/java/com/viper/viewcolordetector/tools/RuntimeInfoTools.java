package com.viper.viewcolordetector.tools;

import android.app.ActivityManager;
import android.content.Context;

/**
 * created by viper on 2021/6/7
 * desc
 */
public class RuntimeInfoTools {

    public static boolean isLowMemo(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        return info.lowMemory;
    }
}
