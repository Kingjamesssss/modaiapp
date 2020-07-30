package com.example.service_test3.helper;

import android.app.Activity;
import android.view.View;

import com.example.service_test3.callback.LogincheckListener;
import com.example.service_test3.db.ModaiDB;

public class LoginCheck implements LogincheckListener {
    private ModaiDB modaiDB;
    private String name;

    public LoginCheck(ModaiDB modaiDB,String name) {
        this.modaiDB = modaiDB;
        this.name = name;
    }

    @Override
    public boolean isLogined() {
        String name = modaiDB.get_Username();
        if (name != null && !name.equals("")){
            return  true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean onLoginedClick() {
        modaiDB.ChangeUserStatus(name,0);
        return true;
    }

    @Override
    public void onNoLoginedClick() {

    }
}
