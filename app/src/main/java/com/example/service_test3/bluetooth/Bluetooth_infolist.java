package com.example.service_test3.bluetooth;

import com.example.service_test3.bean.Bluetooth_info;

import java.util.ArrayList;
import java.util.List;

/*蓝牙信息list类
* 属性：蓝牙信息列表
* 方法：
* 1.add_data :添加蓝牙info
* 2.retain: 对比两个蓝牙信息list类中相同mac地址，把相同的信息添加到list并返回*/

public class Bluetooth_infolist {
    List<Bluetooth_info> bt_info = null;

    public Bluetooth_infolist() {
        bt_info = new ArrayList<Bluetooth_info>();

    }
    public void add_data(Bluetooth_info data){
        bt_info.add(data);
    }

    public  List<Bluetooth_info> retain(Bluetooth_infolist otherlist){
        List<Bluetooth_info> ret = new ArrayList<Bluetooth_info>();

        for (Bluetooth_info data1:bt_info){
            for (Bluetooth_info data2:otherlist.bt_info){
                if (data1.getMac_address().equals(data2.getMac_address())){

                    ret.add(data1);
                    break;
                }
            }
        }
        return  ret;
    }
    public void clear(){
        bt_info.clear();
    }
    public void addAll(Bluetooth_infolist otherlist){
        bt_info.addAll(otherlist.bt_info);
    }

    @Override
    public String toString() {
        String ret  = "";
        for (Bluetooth_info data:bt_info){
            ret+= data.getMac_address();
        }
        return  ret;
    }
}
