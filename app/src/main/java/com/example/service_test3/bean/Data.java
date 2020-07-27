package com.example.service_test3.bean;

/*发送请求数据类
* 属性：1.code
*
*
*
* */


public class Data {

    private String code;
    private int heart ;
    private int packageld;
    private int station;
    private int terminal;
    private int timestamp;
    private String type;
    private double value;
    public Data(String code, int heart, int packageld, int station, int terminal, int timestamp, String type, double value) {
        this.code = code;
        this.heart = heart;
        this.packageld = packageld;
        this.station = station;
        this.terminal = terminal;
        this.timestamp = timestamp;
        this.type = type;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getPackageld() {
        return packageld;
    }

    public void setPackageld(int packageld) {
        this.packageld = packageld;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public int getTerminal() {
        return terminal;
    }

    public void setTerminal(int terminal) {
        this.terminal = terminal;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
