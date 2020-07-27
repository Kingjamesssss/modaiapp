package com.example.service_test3.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.service_test3.db.ModaiDB;
import com.example.service_test3.db.MySQLiteOpenHelper;

import net.vidageek.mirror.dsl.Mirror;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/*
创建一个蓝牙类
属性：蓝牙适配器  数据库  sqlitehelper
方法：
1.开启蓝牙功能
2.开启扫描
3.获取本机蓝牙mac地址
*/
public class BlueTooth {
    //获取蓝牙适配器对象
    BluetoothAdapter mBluetoothAdapter;

    public BlueTooth() {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    //开启蓝牙功能
    public boolean startBlueTooth() {
        if (mBluetoothAdapter == null) {
            Log.d("BlueTooth", "开启失败");
            return false;
        }
        if (!mBluetoothAdapter.isEnabled()) {
                 /* Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                  startActivity(i);*/
            mBluetoothAdapter.enable();
            Log.d("BlueTooth", "开启成功");
            setDiscoverableTimeout(0);
        }
        return true;
    }
    //开启扫描
    public void startScan(){

        //如果当前在搜索，就先取消搜索
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        //开启搜索
        boolean flag = mBluetoothAdapter.startDiscovery();
        Log.d("flag", String.valueOf(flag));
    }
    //获取本机mac地址
    public String getBluetoothAddress() {
/*        //区分版本
        if (Build.VERSION.SDK_INT>=22){
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            } catch (Exception ex) {
            }
            return "";
        }else {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            String macAddr = adapter.getAddress();
            return macAddr;
        }*/


        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            try {
                Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                mServiceField.setAccessible(true);

                Object btManagerService = mServiceField.get(bluetoothAdapter);

                if (btManagerService != null) {
                    bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                }
            } catch (NoSuchFieldException e) {

            } catch (NoSuchMethodException e) {

            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
        } else {
            bluetoothMacAddress = bluetoothAdapter.getAddress();
        }
        return bluetoothMacAddress;

//        try {
//            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            Field field = bluetoothAdapter.getClass().getDeclaredField("mService");
//            // 参数值为true，禁用访问控制检查
//            field.setAccessible(true);
//            Object bluetoothManagerService = field.get(bluetoothAdapter);
//            if (bluetoothManagerService == null) {
//                return null;
//            }
//            Method method = bluetoothManagerService.getClass().getMethod("getAddress");
//            Object address = method.invoke(bluetoothManagerService);
//            if (address != null && address instanceof String) {
//                return (String) address;
//            } else {
//                return null;
//            }
//            //抛一个总异常省的一堆代码...
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        Object bluetoothManagerService = new Mirror().on(bluetoothAdapter).get().field("mService");
//        if (bluetoothManagerService == null) {
//            Log.w("1", "couldn't find bluetoothManagerService");
//            return null;
//        }
//        Object address = new Mirror().on(bluetoothManagerService).invoke().method("getAddress").withoutArgs();
//        if (address != null && address instanceof String) {
//            Log.w("2", "using reflection to get the BT MAC address: " + address);
//            return (String) address;
//        } else {
//            return null;
//        }
    }



    //反射
    private void setDiscoverableTimeout(int timeout) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);

            setDiscoverableTimeout.invoke(adapter, timeout);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
