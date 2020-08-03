package com.example.service_test3.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.service_test3.R;
import com.example.service_test3.adapter.TempAdapter;
import com.example.service_test3.bean.Request_Temp;
import com.example.service_test3.bean.Temp_return;
import com.example.service_test3.callback.HttpCallbackListener;
import com.example.service_test3.db.ModaiDB;
import com.example.service_test3.net.*;

import java.util.LinkedList;
import java.util.List;

public class ShowTempActivity extends AppCompatActivity {
    private List<Temp_return> mData = null;
    private Context mContext;
    private TempAdapter mAdapter = null;
    private ListView listView;
    private HttpRequest httpRequest;
    private String mday;
    private ModaiDB modaiDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_temp);
        Intent intent = getIntent();
        modaiDB = ModaiDB.getInstance(this);
        mday = intent.getStringExtra("day");
        mContext = this;
        listView = (ListView) findViewById(R.id.list_view);
        mData = new LinkedList<Temp_return>();
        Request_Temp temp = Create_Request_Temp(mday);
        httpRequest = new HttpRequest();
        httpRequest.Send_Request_Temp(mData,temp,new HttpCallbackListener(){
            @Override
            public void onSuccess(String response) {
                //回主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateListView();
                    }
                });
            }
            @Override
            public void onError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShowTempActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                        Log.d("info","获取失败");
                    }
                });
            }
        });
        mAdapter = new TempAdapter(mContext,mData);
        listView.setAdapter(mAdapter);

    }
    private void updateListView(){
        //回主线程处理逻辑
        mAdapter.notifyDataSetChanged();
    }
    private Request_Temp Create_Request_Temp(String day){
        return new Request_Temp(modaiDB.get_Username(),"temp_check",Integer.parseInt(day));
    }
}
