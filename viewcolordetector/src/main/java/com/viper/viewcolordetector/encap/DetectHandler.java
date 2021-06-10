package com.viper.viewcolordetector.encap;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.viper.viewcolordetector.DetectClient;

import static com.viper.viewcolordetector.DetectClient.LOG_TAG;


/**
 * created by viper on 2021/6/6
 * desc
 */
public class DetectHandler extends Handler {

    public DetectHandler(@NonNull Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (msg.obj instanceof DetectClient) {
            long start = System.currentTimeMillis();
            DetectClient data = (DetectClient) msg.obj;
            if (data.memo.isLowMemo(data.getView().getContext())) {
                Log.d(LOG_TAG, "低内存，检测取消");
                return;
            }
            Bitmap bm = data.cut.onCut(data.getView());
            if (data.detect.onDetect(bm)) {
                if (!data.bmSave.onBitmapSave(bm, data.getView().getContext()))
                    Log.d(LOG_TAG, "Bitmap保存失败");
                if (!data.activitySave.onActivitySave(data.getActivity(), data.start))
                    Log.d(LOG_TAG, "Activity的信息保存失败");
            }
            if (bm != null && !bm.isRecycled()) bm.recycle();
            Log.d(LOG_TAG, "检测耗时" + (System.currentTimeMillis() - start) + "ms");
        } else Log.d(LOG_TAG, "数据参数错误，检测取消");
    }
}
