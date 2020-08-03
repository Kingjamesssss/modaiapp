package com.example.service_test3.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.service_test3.R;
import com.example.service_test3.Service.IntentServiceDemo;
import com.example.service_test3.bean.UserData;
import com.example.service_test3.callback.HttpCallbackListener;
import com.example.service_test3.db.ModaiDB;
import com.example.service_test3.helper.LoginCheck;
import com.example.service_test3.net.HttpRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public int pwdresetFlag=0;
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码
    private Button mLoginButton;                      //登录按钮
    private Button mCancleButton;                     //注销按钮

    private String mymac_address;//本机mac地址
    private String userNameValue,passwordValue;
    private Handler handler;
    private View loginView;                           //登录
    private View loginSuccessView;
    private TextView loginSuccessShow;
    private HttpRequest httpRequest;
    private ModaiDB modaiDB;
    private UserData userData;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //通过id找到相应的控件
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        mCancleButton = (Button) findViewById(R.id.login_btn_cancle);
        loginView=findViewById(R.id.login_view);
        loginSuccessView=findViewById(R.id.login_success_view);
        loginSuccessShow=(TextView) findViewById(R.id.login_success_show);
        //获取数据库
        modaiDB = ModaiDB.getInstance(this);

        //采用OnClickListener方法设置不同按钮按下之后的监听事件
        mLoginButton.setOnClickListener(this);
        mCancleButton.setOnClickListener(this);
        ImageView image = (ImageView) findViewById(R.id.logo);             //使用ImageView显示logo
        image.setImageResource(R.drawable.denglu);
        Intent intent = getIntent();
        handler = new Handler()
        {
            public void handleMessage(Message msg){

            }

        };

//        mymac_address = intent.getStringExtra("mymac_address");
//        assert mymac_address != null;
//        Log.d("mymac",mymac_address);


    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.login_btn_login:                              //登录界面的登录按钮
                login();
                break;
            case R.id.login_btn_cancle:                             //登录界面的注销按钮
                break;
            default:
                break;
        }
    }
    public void login() {                                              //登录按钮监听事件
        if (isUserNameAndPwdValid()) {

            final String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();
            LoginCheck loginCheck = new LoginCheck(modaiDB,userName);//判断是否登录
            if(!loginCheck.isLogined()) {
                userData = new UserData(userName, userPwd);
                httpRequest = new HttpRequest();
                httpRequest.Send_Login(userData, 0, new HttpCallbackListener() {
                    @Override
                    public void onSuccess(String response) {
//                    Toast.makeText(LoginActivity.this, "登陆成功",
//                            Toast.LENGTH_SHORT).show();
                        //输入用户名和密码正确，判断是否绑定了mac地址
                        if (response.equals("exist")) {
                            if (modaiDB.CheckUserexist(userName)) {
                                modaiDB.ChangeUserStatus(userName, 1);

                            } else
                                modaiDB.SaveUserinfo(userData);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", userName);
                            startActivity(intent);
                        } else {
                            handler.post(Bindmac_check_Dialog);
                        }
                    }


                    @Override
                    public void onError() {
                        Log.d("login", "失败");
                    }
                });
            }else{
                handler.post(Login_dialog);
            }
        }
    }

    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, "用户名不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "电话不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    Runnable Bindmac_Dialog = new Runnable() {
        @Override
        public void run() {
            //先通过 AlertDialog.Builder创建出一个 AlertDialog的实例.
            View v=getLayoutInflater().inflate(R.layout.dialog,null);
            AlertDialog.Builder dialog = new AlertDialog.Builder (LoginActivity.this);
            //然后为这个对话框设 置标题、内容、可否取消等属性
            final EditText et = (EditText)v.findViewById(R.id.edittext_dialog);
            dialog.setView(v);
            // 接下来调用 setPositiveButton()方法为对话框设置确定按钮点击事件
            dialog.setPositiveButton("OK", new DialogInterface. OnClickListener() {
                public void onClick(DialogInterface dialog, int which)
                {
                    String mac = et.getText().toString();
                    System.out.println(mac);
                    userData.setUserMac(mac);
                    userData.setCid(IntentServiceDemo.clientid);
                    httpRequest.Send_Login(userData,1, new HttpCallbackListener() {
                        @Override
                        public void onSuccess(String response) {
                            if (modaiDB.CheckUserexist(userData.getUserName())) {
                                modaiDB.ChangeUserStatus(userData.getUserName(), 1);

                            }
                            else
                                modaiDB.SaveUserinfo(userData);
                            Intent intent  = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("username",userData.getUserName());
                            startActivity(intent);
                        }


                        @Override
                        public void onError() {
                            Log.d("login","失败");
                        }
                    });
                }
            });
            //调用 setNegativeButton()方法设置取消按钮的点击事件。
            dialog.setNegativeButton("Cancel", new DialogInterface. OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            //最后调用 show()方法 将对话框显示出来。
            dialog.show();
        }
    };

    Runnable Bindmac_check_Dialog = new Runnable() {
        @Override
        public void run() {
            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("您还未绑定MAC地址")
                    .setContentText("请先绑定")
                    .setCancelText("不绑定了")
                    .setConfirmText("好的")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener(){
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            handler.post(Bindmac_Dialog);
                        }
                    })
                    .show();
        }
    };
    Runnable Login_dialog = new Runnable() {
        @Override
        public void run() {
            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("您已经登录")
                    .setContentText("请退出当前账号")
                    .setConfirmText("好的")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener(){
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
