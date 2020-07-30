package com.example.service_test3.callback;

import android.app.Activity;
import android.view.View;

public interface LogincheckListener {
    public abstract boolean isLogined();
    public abstract boolean onLoginedClick();
    public abstract void onNoLoginedClick();
}
