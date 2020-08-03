package com.example.service_test3.helper;

import com.example.service_test3.callback.LogincheckListener;
import com.example.service_test3.db.ModaiDB;

public class LoginCheck implements LogincheckListener {
    private ModaiDB modaiDB;

    public LoginCheck(ModaiDB modaiDB) {
        this.modaiDB = modaiDB;

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
        String name = modaiDB.get_Username();
        modaiDB.ChangeUserStatus(name,0);
        return true;
    }

    @Override
    public void onNoLoginedClick() {

    }
}
