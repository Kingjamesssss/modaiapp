package com.example.service_test3.bean;

/*返回数据类
* 属性：
* 1.体温值
* 2.时间戳
* 3.地点
* 4.报警信息
* 5.图片id
* */

public class Temp_return {
    private String value;
    private int timestamp;
    private String location;
    private int warn;
    private int image;

    public int getWarn() {
        return warn;
    }

    public void setWarn(int warn) {
        this.warn = warn;
    }

    public Temp_return(String value, int timestamp, String location, int image) {
        this.value = value;
        this.timestamp = timestamp;
        this.location = location;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
