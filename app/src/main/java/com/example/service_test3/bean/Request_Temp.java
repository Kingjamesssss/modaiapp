package com.example.service_test3.bean;

public class Request_Temp {
    private String name;
    private String type;
    private int day;

    public Request_Temp(String name, String type, int day) {
        this.name = name;
        this.type = type;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
