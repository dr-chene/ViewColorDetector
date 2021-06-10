package com.viper.viewcolordetector.encap;

import android.os.HandlerThread;
import android.os.Looper;

/**
 * created by viper on 2021/6/6
 * desc HandlerThread的简单封装
 */
public class DetectHandlerThread {
    private final HandlerThread mHandlerThread;

    public DetectHandlerThread() {
        mHandlerThread = new HandlerThread("ViewColorDetectThread");
        start();
    }

    private void start() {
        mHandlerThread.start();
    }

    public void quit() {
        mHandlerThread.quit();
    }

    public Looper getLooper() {
        return mHandlerThread.getLooper();
    }
}