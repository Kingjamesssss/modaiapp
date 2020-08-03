package com.example.service_test3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.service_test3.bean.Bluetooth_info;
import com.example.service_test3.bean.UserData;

import java.util.ArrayList;
import java.util.List;

public class ModaiDB {
    private static final String DB_NAME = "user.db";
    private static final int VERSION = 1;

    private static ModaiDB modaiDB;
    private SQLiteDatabase db;
    private MySQLiteOpenHelper dbHelper;

    //构造方法
    private ModaiDB(Context context){

        dbHelper = new MySQLiteOpenHelper(context);
        db = dbHelper.getWritableDatabase();
        Log.d("DB","success");
    }
    //获取ModaiDB实例
    public synchronized static ModaiDB getInstance(Context context){
        if (modaiDB == null){
            modaiDB = new ModaiDB(context);
        }
        Log.d("DB","创建成功");
        return modaiDB;
    }
    //储存用户信息
    public void SaveUserinfo(UserData userData){
        if (userData != null){
            ContentValues values = new ContentValues();
            values.put("name",userData.getUserName());
            values.put("pwd",userData.getUserPwd());
            values.put("status",userData.getStatus());
            values.put("mymac_address",userData.getUserMac());
            long i;
            if ((i = db.insert("User", null, values)) == -1) {
                Log.d("DB", "储存信息错误");
            }
            Log.d("DB", "用户信息已经储存");
        }
    }
    //修改用户信息状态
    public void ChangeUserStatus(String name,int flag){
        if (flag == 0) {
            ContentValues values = new ContentValues();
            values.put("status", 0);
            db.update("User", values, "name=?", new String[]{name});
        }else{
            ContentValues values = new ContentValues();
            values.put("status", 1);
            db.update("User", values, "name=?", new String[]{name});
        }
        Log.d("用户信息","修改成功");
    }
    public boolean CheckUserexist(String name){
        Cursor c = db.query("User",null,"name=?", new String[]{name},null,null,null);
        if (c.getCount() == 0) {
            Log.d("用户信息", "不存在");
            return false;
        }
        else{
            Log.d("用户信息", "存在");
            return true;
        }

    }
    //显示用户信息
    public void Show_Userinfo(){
        Cursor c = db.query("User",null,null, null,null,null,null);
        if(c.moveToFirst()){
            do{
                Log.d("姓名",c.getString(c.getColumnIndex("name")));
                Log.d("密码",c.getString(c.getColumnIndex("pwd")));
                Log.d("密码",c.getString(c.getColumnIndex("status")));
            }while (c.moveToNext());
        }
        c.close();
    }
    //获取用户信息
    public String get_Username(){
        String username = null ;
        Cursor c = db.query("User",null,"status=1", null,null,null,null);
        if(c.moveToFirst()){
            do{
                username = c.getString(c.getColumnIndex("name"));
                break;
            }while (c.moveToNext());
        }
        c.close();

        return username;
    }
    //返回一个体温请求数据的信息

    //储存蓝牙信息
    public void SavaBlueToothinfo(Bluetooth_info bluetooth_info){
        if (bluetooth_info != null){
            ContentValues values = new ContentValues();
            values.put("mac_address", bluetooth_info.getMac_address());
            values.put("timestamp", bluetooth_info.getTimestamp());
            long i;
            if ((i = db.insert("Bluetooth", null, values)) == -1) {
                Log.d("DB", "储存信息错误");
            }
            Log.d("DB", "蓝牙信息已经储存");
        }
    }
    //显示蓝牙数据
    public void Show_Bluetoothinfo(){
        Cursor c = db.query("Bluetooth",null,null,null,null,null,null);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            Log.d("mac地址",c.getString(c.getColumnIndex("mac_address")));
            Log.d("Time",c.getString(c.getColumnIndex("timestamp")));
        }
    }
    //获取数据库蓝牙数据列表
    public List<Bluetooth_info> Get_BluetoothinfoList(){
        List<Bluetooth_info> bluetooth_infoList = new ArrayList<Bluetooth_info>();
        Bluetooth_info bluetooth_info;
        String mac_address;
        int timestamp;
        Cursor c = db.query("Bluetooth",null,null,null,null,null,null);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            mac_address = c.getString(c.getColumnIndex("mac_address"));
            timestamp = c.getInt(c.getColumnIndex("timestamp"));
            bluetooth_info = new Bluetooth_info(mac_address,timestamp);
            bluetooth_infoList.add(bluetooth_info);
        }
        return bluetooth_infoList;
    }
    //删除表中数据
    public void deleteinfo(){
        db.delete("Bluetooth",null,null);
    }

}
