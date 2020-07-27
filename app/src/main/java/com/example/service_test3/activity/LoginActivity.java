package com.example.service_test3.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.service_test3.R;
import com.example.service_test3.bean.Login_return;
import com.example.service_test3.bean.Request_Temp;
import com.example.service_test3.bean.UserData;
import com.example.service_test3.callback.HttpCallbackListener;

import com.example.service_test3.db.ModaiDB;
import com.example.service_test3.helper.object_json;
import com.example.service_test3.net.HttpRequest;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public int pwdresetFlag=0;
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码
    private Button mLoginButton;                      //登录按钮
    private Button mCancleButton;                     //注销按钮

    private String mymac_address;//本机mac地址
    private String userNameValue,passwordValue;

    private View loginView;                           //登录
    private View loginSuccessView;
    private TextView loginSuccessShow;

    private ModaiDB modaiDB;

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
                cancel();
                break;
            default:
                break;
        }
    }
    public void login() {                                              //登录按钮监听事件
        if (isUserNameAndPwdValid()) {

            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();
            final UserData userData = new UserData(userName,userPwd,"123");
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.Send_Login(userData, new HttpCallbackListener() {
                @Override
                public void onSuccess(String response) {
                    Toast.makeText(LoginActivity.this, "登陆成功",
                            Toast.LENGTH_SHORT).show();
                    Intent intent  = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    modaiDB.SaveUserinfo(userData);
                }

                @Override
                public void onError() {
                    Log.d("login","失败");
                }
            });
        }
    }
    public void cancel() {           //注销
/*        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();
            int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if(result==1){                                             //返回1说明用户名和密码均正确
                Toast.makeText(this, getString(R.string.cancel_success),Toast.LENGTH_SHORT).show();<span style="font-family: Arial;">//注销成功提示</span>
                        mPwd.setText("");
                mAccount.setText("");
                mUserDataManager.deleteUserDatabyname(userName);
            }else if(result==0){
                Toast.makeText(this, getString(R.string.cancel_fail),Toast.LENGTH_SHORT).show();  //注销失败提示
            }
        }*/

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
