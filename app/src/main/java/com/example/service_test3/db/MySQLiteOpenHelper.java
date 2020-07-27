package com.example.service_test3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*数据库帮助类
* 属性：
* 1.DB_NAME 数据库名
* 2.VERSION 版本
* */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "user.db";
    private static final int VERSION = 1;

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    //数据库字段：1.名字  2.MAC地址
    public void onCreate(SQLiteDatabase db) {
        //数据库创建
        db.execSQL("create table Bluetooth (_id integer primary key autoincrement, " +
                "mac_address char(30),timestamp int(20))");
        Log.d("DB","创建蓝牙表成功");
        db.execSQL("create table User (_id integer primary key autoincrement, " +
                "name char(20),pwd char(20),mymac_address char(30),status int(1))");
        Log.d("DB","创建用户表成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库升级
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}