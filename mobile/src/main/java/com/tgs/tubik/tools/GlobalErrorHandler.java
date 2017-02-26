package com.tgs.tubik.tools;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.tgs.tubik.TubikApp;

public class GlobalErrorHandler implements Thread.UncaughtExceptionHandler {

    private TubikApp mApp;

    private static Thread.UncaughtExceptionHandler mDefaultHandler = Thread
        .getDefaultUncaughtExceptionHandler();

    public GlobalErrorHandler(Application app) {
        mApp = (TubikApp) app;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            handleUncaughtException(thread, ex);
        } catch(Exception e) {
            e.printStackTrace();
            // let the native handler do its job
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    public boolean isUIThread(){
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        if(isUIThread()) {
            invokeLogActivity();
        } else {  // handle non UI thread throw uncaught exception
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    invokeLogActivity();
                }
            });
        }
    }

    private void invokeLogActivity(){
        Intent intent = new Intent ("com.tgs.tubik.SEND_LOG");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mApp.startActivity(intent);
    }
}