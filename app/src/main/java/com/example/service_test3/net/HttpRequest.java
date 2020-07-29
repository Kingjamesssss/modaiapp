package com.example.service_test3.net;

import android.os.Handler;

import com.example.service_test3.bean.Bluetooth_return;
import com.example.service_test3.bean.Login_return;
import com.example.service_test3.bean.Request_Temp;
import com.example.service_test3.bean.UserData;
import com.example.service_test3.bean.User_tempinfo;
import com.example.service_test3.callback.HttpCallbackListener;
import com.example.service_test3.db.ModaiDB;
import com.example.service_test3.bean.Temp_return;
import com.example.service_test3.helper.object_json;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
/*
http请求类
属性：handler
方法：1.请求xxxx数据

*/
public class HttpRequest {
    private Handler handler;
//    private ModaiDB modaiDB;
    public HttpRequest(Handler handler) {
        this.handler = handler;
    }
    public HttpRequest() {
    }


//发送POST请求体温信息
public void Send_Request_Temp(final List<Temp_return> mData, final Request_Temp temp, final HttpCallbackListener listener){
    new Thread(new Runnable() {
        @Override
        public void run() {
            HttpURLConnection connection = null;
            try {
                //创建URL对象
                URL url = new URL(urlapi.Request_temp);
                //设置请求的属性

                connection = (HttpURLConnection) url.openConnection();
                //因为是POST请求，设置输出和输入允许
                connection.setDoOutput(true);
                connection.setDoInput(true);
                //设置成POST模式
                connection.setRequestMethod("POST");
                //POST请求不能使用缓存
                connection.setUseCaches(false);
                connection.setInstanceFollowRedirects(true);
                //配置本次连接的Content-Type
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(5000);
                connection.connect();
                //POST
                //创建输出流
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                //创建json格式的数据结构，为key-value结构
//                Request_Temp temp = new Request_Temp(,"temp_check",10);
                //把对象转化成json串
                String json = object_json.get_temp_Jsonstr(temp);
                System.out.println(json);
                out.writeBytes(json);
                out.flush();
                out.close();
                //获取服务器返回的输入流(字节流转字符流）
                Map<String, List<String>> map = connection.getHeaderFields();
                for (String key : map.keySet()) {
                    System.out.println(key + ": " + map.get(key));
                }
                InputStream in = connection.getInputStream();
                //字节流
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                //创建一个StringBuilder（防止产生内存浪费）
                StringBuilder response = new StringBuilder();
                String line;
                while((line=reader.readLine())!=null){
                    response.append(line);
                }
                User_tempinfo user_tempinfo = object_json.handleReturn_tempinfo(String.valueOf(response),mData);
                System.out.println(user_tempinfo.getStatus());
                if (user_tempinfo.getStatus() == 1000){
                    listener.onSuccess(response.toString());
                }else{
                    listener.onError();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection!=null){
                    connection.disconnect();
                }

            }

        }
    }).start();
}
    //发送蓝牙信息
    public void Send_Bluetoothinfo(final ModaiDB modaiDB,final HttpCallbackListener listener) throws JSONException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    //创建URL对象
                    URL url = new URL(urlapi.Send_Bluetoothinfo);
                    //设置请求的属性

                    connection = (HttpURLConnection) url.openConnection();
                    //因为是POST请求，设置输出和输入允许
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    //设置成POST模式
                    connection.setRequestMethod("POST");
                    //POST请求不能使用缓存
                    connection.setUseCaches(false);
                    connection.setInstanceFollowRedirects(true);
                    //配置本次连接的Content-Type
                    connection.setRequestProperty("Content-Type", "application/json");
                    /*                    connection.setRequestMethod("GET");*/
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    //Todo something
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    String json = object_json.get_Bluetoothinfo_Jsonstr(modaiDB);
                    System.out.println(json);
                    out.writeBytes(json);
                    out.flush();
                    out.close();
                    InputStream in = connection.getInputStream();
                    //字节流
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    //创建一个StringBuilder（防止产生内存浪费）
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    Bluetooth_return bluetooth_return = object_json.parsetoBluetooth_return(response.toString());
                    if (bluetooth_return.getStatus() == 1000){
                        listener.onSuccess(response.toString());
                    }else{
                        listener.onError();
                    }


                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //发送登录信息
    public void Send_Login(final UserData data, final int type, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    //创建URL对象
                    URL url = new URL(urlapi.Login);
                    //设置请求的属性

                    connection = (HttpURLConnection) url.openConnection();
                    //因为是POST请求，设置输出和输入允许
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    //设置成POST模式
                    connection.setRequestMethod("POST");
                    //POST请求不能使用缓存
                    connection.setUseCaches(false);
                    connection.setInstanceFollowRedirects(true);
                    //配置本次连接的Content-Type
                    connection.setRequestProperty("Content-Type", "application/json");
                    /*                    connection.setRequestMethod("GET");*/
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    //POST
                    //创建输出流
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    //创建json格式的数据结构，为key-value结构

                    //把对象转化成json串
                    String json = object_json.get_Login_Jsonstr(data,type);
                    System.out.println(json);
                    out.writeBytes(json);
                    out.flush();
                    out.close();

                    InputStream in = connection.getInputStream();
                    //字节流
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    //创建一个StringBuilder（防止产生内存浪费）
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    Login_return ret = object_json.parsetoLogin_return(response.toString());
                    System.out.println(ret.getMac());
                    //如果成功
                    if(ret.getStatus() == 1000){
                        data.setStatus(1);
                        if (listener != null ){
                            listener.onSuccess(ret.getMac());
                        }
                    }else{
                        if (listener != null ){
                            listener.onError();
                        }
                    }


                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }).start();
    }


}
