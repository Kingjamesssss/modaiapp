package com.example.service_test3.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.service_test3.R;
import com.example.service_test3.adapter.TempAdapter;
import com.example.service_test3.bean.Temp_return;
import com.example.service_test3.callback.HttpCallbackListener;
import com.example.service_test3.net.HttpRequest;

import java.util.LinkedList;
import java.util.List;

public class ShowTempActivity extends AppCompatActivity {
    private List<Temp_return> mData = null;
    private Context mContext;
    private TempAdapter mAdapter = null;
    private ListView listView;
    private HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_temp);
        mContext = this;
        listView = (ListView) findViewById(R.id.list_view);
        mData = new LinkedList<Temp_return>();
        httpRequest = new HttpRequest();

        httpRequest.Request_Temp(mData,new HttpCallbackListener(){
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
        mData.add(new Temp_return("12.1",12313,"南二门",R.drawable.denglu));
        mData.add(new Temp_return("12.1",12313,"南二门",R.drawable.denglu));
        mAdapter = new TempAdapter(mContext,mData);
        listView.setAdapter(mAdapter);

    }
    private void updateListView(){
        //回主线程处理逻辑
        mAdapter.notifyDataSetChanged();
    }
}
