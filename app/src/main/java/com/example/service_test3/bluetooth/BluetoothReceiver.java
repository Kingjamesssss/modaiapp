package com.example.service_test3.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.service_test3.db.ModaiDB;
import com.example.service_test3.db.MySQLiteOpenHelper;
import com.example.service_test3.bean.Bluetooth_info;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*创建一个蓝牙广播接收器
* 属性：
* 1.本地数据库和helper
* 2.两个蓝牙MAC地址储存列表
*
* 方法：
* 重写onReceive
* 1.
*
* */
public class BluetoothReceiver extends BroadcastReceiver {
    private ModaiDB modaiDB ;
//    SQLiteDatabase db;
//    MySQLiteOpenHelper helper;
    //创建两个蓝牙设备列表，1是上一次的蓝牙设备，2是这一次扫描的蓝牙设备
    private Bluetooth_infolist bt_list1 = new Bluetooth_infolist();
    private Bluetooth_infolist bt_list2 = new Bluetooth_infolist();
    private Bluetooth_info bt_info;


//    public BluetoothReceiver(SQLiteDatabase db, MySQLiteOpenHelper helper) {
//        this.db = db;
//        this.helper = helper;

    public BluetoothReceiver(ModaiDB modaiDB) {
        this.modaiDB = modaiDB;

    }
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        ContentValues cv = new ContentValues();
        Log.d("service","有回调");
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            assert device != null;
            if (device.getName()!=null) {
                Log.d("device","name:"+device.getName()+"address"+device.getAddress());
                //已匹配的设备的mac地址，时间  ,添加到蓝牙设备储存list中
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                //获取当前时间
                Date date = new Date(System.currentTimeMillis());
//                String time = simpleDateFormat.format(date);
                //获取时间戳
                String timestamp = String.valueOf(date.getTime()/1000);
                bt_info = new Bluetooth_info(device.getAddress(),Integer.parseInt(timestamp));
                bt_list2.add_data(bt_info);
            }
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            Log.d("device", "开始搜索");
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            //比较上一次匹配到的蓝牙列表和这一次的蓝牙列表，一样的就储存到数据库中

            List<Bluetooth_info> ret = bt_list1.retain(bt_list2);
            if (ret != null) {
                for (Bluetooth_info info : ret) {
                    //将蓝牙信息储存
                    modaiDB.SavaBlueToothinfo(info);
                }
            }
            bt_list1.clear();
            bt_list1.addAll(bt_list2);
            Log.d("device", "搜索完毕");
        }
    }

}


