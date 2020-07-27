package com.example.service_test3.bean;


/*数据库储存的蓝牙数据类
* 1.Mac_address
* 2.timestamp
* */
public class Bluetooth_info {
    String mac_address;
//    String time;
    int timestamp;

    public Bluetooth_info(String mac_address, int timestamp) {
        this.mac_address = mac_address;
        this.timestamp = timestamp;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
