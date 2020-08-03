package com.example.service_test3.helper;

import com.example.service_test3.R;
import com.example.service_test3.bean.Bluetooth_info;
import com.example.service_test3.bean.Bluetooth_return;
import com.example.service_test3.bean.Login_return;
import com.example.service_test3.bean.Request_Temp;
import com.example.service_test3.bean.Temp_return;
import com.example.service_test3.bean.UserData;
import com.example.service_test3.bean.User_tempinfo;
import com.example.service_test3.db.ModaiDB;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


/*java类和json互转工具类
* 方法：
* 1.getJsonstr  获取json串
* 2.parseJSONwithGSON  json串转对象
* */
public class object_json {


/*    public static String getJsonstr(Data stu) throws JSONException {
        //先创建一个JSONObject
        JSONObject obj = new JSONObject();
        //创建一个JSON数组，里面再创建一个JSONObject，可以进行嵌套
        JSONArray array = new JSONArray();
        //再创建一个JSONObject，丢到JSON数组中，可以进行嵌套
        JSONObject data = new JSONObject();
        data.put("code",stu.getCode());
        data.put("heart",stu.getHeart());
        data.put("packageld",stu.getPackageld());
        data.put("station",stu.getStation());
        data.put("terminal",stu.getTerminal());
        data.put("timestamp",stu.getTimestamp());
        data.put("type",stu.getType());
        data.put("value",stu.getValue());
        array.put(data);
        obj.put("data",array);
        return obj.toString();
    }*/
    //获得上发蓝牙信息的json数据
    public static String get_Bluetoothinfo_Jsonstr(ModaiDB modaiDB) throws JSONException {
        //先创建一个JSONObject
        JSONObject obj = new JSONObject();
        //创建一个JSON数组，里面再创建一个JSONObject，可以进行嵌套
        JSONArray array = new JSONArray();
        List<Bluetooth_info> bluetooth_infoList = modaiDB.Get_BluetoothinfoList();
        for (Bluetooth_info bluetooth_info : bluetooth_infoList){
            //再创建一个JSONObject，丢到JSON数组中，可以进行嵌套
            JSONObject data = new JSONObject();
            data.put("mac_address",bluetooth_info.getMac_address());
            data.put("time",bluetooth_info.getTimestamp());
            array.put(data);
        }
        obj.put("name",modaiDB.get_Username());
        obj.put("data",array);
        return obj.toString();
    }
    //获得请求体温的json数据
    public static String get_temp_Jsonstr(Request_Temp data) throws JSONException {
        //先创建一个JSONObject
        JSONObject info = new JSONObject();
        info.put("name",data.getName());
        info.put("type",data.getType());
        info.put("day",data.getDay());
        return info.toString();
    }

    //获得用户信息的json数据
    public static String get_Login_Jsonstr(UserData data,int type) throws JSONException, NoSuchAlgorithmException {
        JSONObject info = new JSONObject();
        info.put("name",data.getUserName());
        info.put("pwd",data.getUserPwd());
        info.put("usermac",data.getUserMac());
        info.put("cid",data.getCid());
        info.put("type",type);
        String str = data.getUserName()+"#"+data.getUserPwd();
        MessageDigest md5 = null;
        md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(str.getBytes());
        String result = "";
        for (byte b : bytes){
            String temp = Integer.toHexString(b & 0xff);
            if(temp.length() == 1){
                temp = "0" + temp;
            }
            result += temp;
        }
        info.put("key",result);
        return info.toString();
    }



    //解析登陆请求返回的数据
    public static Login_return parsetoLogin_return(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, Login_return.class);
    }
    //解析服务器返回的体温的数据
    public static User_tempinfo parsetoUser_tempinfo(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, User_tempinfo.class);
    }
    //解析蓝牙数据上传返回的数据
    public static Bluetooth_return parsetoBluetooth_return(String jsonData){
        Gson gson = new Gson();
        return gson.fromJson(jsonData, Bluetooth_return.class);
    }



    //处理服务器返回的温度信息
    public static User_tempinfo handleReturn_tempinfo(String response, List<Temp_return> mData){
        User_tempinfo user_tempinfo =  object_json.parsetoUser_tempinfo(response);
        for (Temp_return temp_return:user_tempinfo.getResults()){
            if (temp_return.getWarn()==0){
                temp_return.setImage(R.drawable.safe);
            }else{
                temp_return.setImage(R.drawable.unsafe);
            }
            mData.add(temp_return);
        }
        return user_tempinfo;
    }


}
