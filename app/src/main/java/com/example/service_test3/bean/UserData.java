package com.example.service_test3.bean;

public class UserData {
    private String userName;
    private String userPwd;
    private String userMac;
    private int status ;

    public UserData(String userName, String userPwd, String userMac) {
        this.userName = userName;
        this.userPwd = userPwd;
        this.userMac = userMac;
    }

    public String getUserMac() {
        return userMac;
    }

    public void setUserMac(String userMac) {
        this.userMac = userMac;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
