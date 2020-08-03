package com.example.service_test3.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.service_test3.R;
import com.example.service_test3.Service.MyService;
import com.example.service_test3.bluetooth.BlueTooth;
import com.example.service_test3.callback.HttpCallbackListener;
import com.example.service_test3.db.ModaiDB;
import com.example.service_test3.helper.LoginCheck;
import com.example.service_test3.net.*;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public static final int SHOW_RESPONSE = 0;
    private String username=null;  // 用户名字
    private TextView responseText = null;
    private EditText mday = null;//搜寻几天内的记录
    private HttpRequest request;
    //用于获取本机的mac地址
    private BlueTooth blueTooth;
    private ModaiDB modaiDB;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    responseText.setText(response);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blueTooth = new BlueTooth();
        modaiDB = ModaiDB.getInstance(this);
        Button send_blue = (Button) findViewById(R.id.send_bluetooth);//发送蓝牙信息
        Button exit = (Button) findViewById(R.id.exit);//退出登录
        mday = (EditText) findViewById(R.id.day);

/*        Button startService = (Button) findViewById(R.id.start_service);
        Button stopService = (Button) findViewById(R.id.stop_service);*/
        Button Connect = (Button) findViewById(R.id.request);
        Button login_activity = (Button) findViewById(R.id.login_activity);
        Button showUser = (Button) findViewById(R.id.showUser);
        responseText = (TextView) findViewById(R.id.Response);
/*
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);*/
        send_blue.setOnClickListener(this);
        exit.setOnClickListener(this);
        Connect.setOnClickListener(this);
        login_activity.setOnClickListener(this);
        showUser.setOnClickListener(this);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i("TAG", "sdk < 28 Q");
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] strings =
                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                String[] strings = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"};
                ActivityCompat.requestPermissions(this, strings, 2);
            }
        }
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Intent startIntent = new Intent(this, MyService.class);
        startService(startIntent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request:
                Intent ShowTempIntent = new Intent(this, ShowTempActivity.class);

                String day = "";
                day = mday.getText().toString();
                if (!day.equals("")) {
                    ShowTempIntent.putExtra("day", day);
                    startActivity(ShowTempIntent);
                } else {
                    Toast.makeText(this, "请输入日期", Toast.LENGTH_SHORT).show();
                    //                    Toast.makeText(LoginActivity.this, "登陆成功",
//                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_activity:
                Intent LoginIntent = new Intent(this, LoginActivity.class);
//                String mac = blueTooth.getBluetoothAddress();
//                Log.d("macaddress",mac);
//                LoginIntent.putExtra("mymac_address",blueTooth.getBluetoothAddress());
//                Toast.makeText(this,mac,Toast.LENGTH_SHORT).show();
                startActivity(LoginIntent);
                break;
            case R.id.showUser:
                modaiDB.Show_Userinfo();
                break;
            case R.id.send_bluetooth:
                HttpRequest httpRequest = new HttpRequest();
                try {
                    httpRequest.Send_Bluetoothinfo(modaiDB, new HttpCallbackListener() {
                        @Override
                        public void onSuccess(String response) {
                            modaiDB.deleteinfo();
                        }

                        @Override
                        public void onError() {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.exit:
                if (Loginexit())
                    Toast.makeText(this,"已退出",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"退出失败",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    //安卓6回调（有bug）
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivit", "回调request");
                    //TODO request success/
                }
                break;
        }
    }

    public boolean Loginexit() {
        LoginCheck loginCheck = new LoginCheck(modaiDB);
        boolean check = loginCheck.isLogined();
        if (check) {
            loginCheck.onLoginedClick();
            return true;
        } else {
            loginCheck.onNoLoginedClick();
            return false;
        }

    }

//    private void check_Login(){
//        modaiDB.
//
//    }

}