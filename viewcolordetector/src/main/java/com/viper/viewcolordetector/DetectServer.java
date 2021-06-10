package com.viper.viewcolordetector;

import android.os.Handler;
import android.os.Message;

import com.viper.viewcolordetector.encap.DetectHandler;
import com.viper.viewcolordetector.encap.DetectHandlerThread;

/**
 * created by viper on 2021/6/6
 * desc detect服务，使用handler提交检测任务到HandlerThread
 */
class DetectServer {

    private static volatile DetectServer instance = null;
    private final Handler mHandler;
    private final DetectHandlerThread mHandlerThread;

    private DetectServer() {
        mHandlerThread = new DetectHandlerThread();
        mHandler = new DetectHandler(mHandlerThread.getLooper());
    }

    public static DetectServer getInstance() {
        if (instance == null) {
            synchronized (DetectServer.class) {
                if (instance == null) {
                    instance = new DetectServer();
                }
            }
        }
        return instance;
    }

    protected void remove(int what) {
        mHandler.removeMessages(what);
    }

    protected void detect(DetectClient data, long delay, int what) {
        Message msg = mHandler.obtainMessage();
        msg.obj = data;
        msg.what = what;
        mHandler.sendMessageDelayed(msg, delay);
    }

    public void quit() {
        mHandlerThread.quit();
        instance = null;
    }
}
