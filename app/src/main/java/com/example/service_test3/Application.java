package com.example.service_test3;

import android.util.Log;

import com.igexin.sdk.IUserLoggerInterface;

public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        com.igexin.sdk.PushManager.getInstance().initialize(this);
        com.igexin.sdk.PushManager.getInstance().setDebugLogger(this, new IUserLoggerInterface() {
            @Override
            public void log(String s) {
                Log.i("PUSH_LOG",s);
            }
        });
    }
}
