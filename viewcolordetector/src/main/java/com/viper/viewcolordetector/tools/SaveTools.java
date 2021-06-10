package com.viper.viewcolordetector.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.viper.viewcolordetector.DetectClient.LOG_TAG;

/**
 * created by viper on 2021/6/6
 * desc
 */
public class SaveTools {

    public static boolean saveBitmap(@NonNull Bitmap bm, @NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri insert = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
            if (insert == null) return false;
            OutputStream os = null;
            try {
                os = context.getContentResolver().openOutputStream(insert);
                if (os == null) return false;
                bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.flush();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bm, "color_error_" + System.currentTimeMillis(), "view color detect error");
        }
        return true;
    }

    public static boolean saveActivityInfo(Activity activity, long start) {
        if (activity == null) return false;
        File file = new File(activity.getFilesDir(), "activity_error_info");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(System.currentTimeMillis());
        //activity名字
        String name = activity.getPackageName() + "." + activity.getLocalClassName();
        long run = System.currentTimeMillis() - start;
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(100);
        boolean isActive = false;
        boolean isTop = false;
        if (info != null) {
            for (int i = 0; i < info.size(); i++) {
                if (info.get(i).topActivity.getClassName().equals(name)) {
                    isActive = true;
                    if (i == 0) {
                        isTop = true;
                    }
                }
            }
        }
        PackageManager pm = activity.getPackageManager();
        int versionCode = 0;
        String versionName = "";
        try {
            if (pm != null) {
                PackageInfo pi = pm.getPackageInfo(activity.getPackageName(), 0);
                versionCode = pi.versionCode;
                versionName = pi.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String str = "发现时间：" + date + "\n" +
                "Activity名字：" + name + "\n" +
                "存在时长：" + run + "ms" + "\n" +
                "是否位与前台：" + isTop + "\n" +
                "是否处于活跃状态：" + isActive + "\n" +
                "版本号：" + versionCode + "\n" +
                "版本名：" + versionName + "\n";
        Log.d(LOG_TAG, "saveActivityInfo: " + str);
        return saveInfoToFile(file, str);
    }

    private static boolean saveInfoToFile(File file, String info) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)
            ));
            out.write(info);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
