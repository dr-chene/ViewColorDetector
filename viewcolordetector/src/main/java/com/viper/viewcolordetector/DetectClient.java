package com.viper.viewcolordetector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.viper.viewcolordetector.tools.DetectTools;
import com.viper.viewcolordetector.tools.RuntimeInfoTools;
import com.viper.viewcolordetector.tools.SaveTools;

/**
 * created by viper on 2021/6/6
 * desc
 */
public class DetectClient implements LifecycleObserver {
    public static final String LOG_TAG = "UIColorDetector";
    private static final int SIZE = 100;
    private static int what = 0;
    public final IDetect detect;
    public final ICut cut;
    public final IMemo memo;
    public final IBitmapSave bmSave;
    public final IActivitySave activitySave;
    private final View mView;
    private final Activity mActivity;
    private final long mDelay;
    private int target;
    public long start;

    private DetectClient(Builder builder) {
        this.mView = builder.view;
        this.mActivity = builder.activity;
        this.mDelay = builder.delay;
        this.detect = builder.detect;
        this.cut = builder.cut;
        this.memo = builder.memo;
        this.bmSave = builder.bmSave;
        this.activitySave = builder.activitySave;
        this.start = builder.start;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void start() {
        if (memo.isLowMemo(mView.getContext())) {
            Log.d(LOG_TAG, "低内存，退出");
            return;
        }
        DetectServer.getInstance().remove(target);
        target = what++ % SIZE;
        DetectServer.getInstance().detect(this, mDelay, target);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void stop() {
        DetectServer.getInstance().remove(target);
        Log.d(LOG_TAG, "检测停止");
    }

    public View getView() {
        return mView;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public interface IDetect {
        boolean onDetect(Bitmap bm);
    }

    public interface ICut {
        Bitmap onCut(View view);
    }

    public interface IMemo {
        boolean isLowMemo(Context context);
    }

    public interface IBitmapSave {
        boolean onBitmapSave(Bitmap bm, Context context);
    }

    public interface IActivitySave {
        boolean onActivitySave(Activity activity, long start);
    }

    public static final class Builder {
        private final View view;
        private long delay;
        private Activity activity;
        private IDetect detect;
        private ICut cut;
        private IMemo memo;
        private IBitmapSave bmSave;
        private IActivitySave activitySave;
        private long start;

        public Builder(@NonNull View view) {
            this.view = view;
            this.detect = DetectTools::onDetect;
            this.cut = DetectTools::onCut;
            this.memo = RuntimeInfoTools::isLowMemo;
            this.bmSave = SaveTools::saveBitmap;
            this.activitySave = SaveTools::saveActivityInfo;
            this.activity = null;
        }

        public Builder activityInfo(@NonNull Activity activity) {
            this.activity = activity;
            start = System.currentTimeMillis();
            return this;
        }

        public Builder delay(long delay) {
            this.delay = delay;
            return this;
        }

        public Builder onDetect(IDetect IDetect) {
            this.detect = IDetect;
            return this;
        }

        public Builder onCut(ICut ICut) {
            this.cut = ICut;
            return this;
        }

        public Builder isLowMemo(IMemo IMemo) {
            this.memo = IMemo;
            return this;
        }

        public Builder onBitmapSave(IBitmapSave save) {
            this.bmSave = save;
            return this;
        }

        public Builder onActivitySave(IActivitySave save) {
            this.activitySave = save;
            return this;
        }

        public DetectClient build() {
            return new DetectClient(this);
        }
    }
}
