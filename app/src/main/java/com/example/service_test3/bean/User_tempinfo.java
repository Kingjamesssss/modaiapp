package com.example.service_test3.bean;

import java.util.List;

public class User_tempinfo {
    private int status;
    private String desc;
    private List<Temp_return> results;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Temp_return> getResults() {
        return results;
    }

    public void setResults(List<Temp_return> results) {
        this.results = results;
    }
}
