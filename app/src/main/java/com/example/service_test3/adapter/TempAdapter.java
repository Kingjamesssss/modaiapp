package com.example.service_test3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.service_test3.R;
import com.example.service_test3.bean.Temp_return;

import java.util.List;

public class TempAdapter  extends BaseAdapter {
    private List<Temp_return> mData;
    private Context mContext;
    public TempAdapter(Context context, List<Temp_return> mData){
        this.mContext = context;
        this.mData = mData;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
/*        ServReturn servReturn = (ServReturn) getItem(position);
        convertView = LayoutInflater.from(mContext).inflate(R.layout.displaytemp,parent,false);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);
        TextView textView = (TextView)convertView.findViewById(R.id.text);
        imageView.setBackgroundResource(mData.get(position).getImage());
        textView.setText(mData.get(position).getLocation());*/

        Temp_return tempreturn = (Temp_return) getItem(position);
        convertView = LayoutInflater.from(mContext).inflate(R.layout.displaytemp,parent,false);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);
        TextView temp_text = (TextView)convertView.findViewById(R.id.temp_text);
        TextView location_text = (TextView)convertView.findViewById(R.id.location_text);
        TextView time_text = (TextView)convertView.findViewById(R.id.time_text);
        imageView.setBackgroundResource(mData.get(position).getImage());
        temp_text.setText(mData.get(position).getValue());
        location_text.setText(mData.get(position).getLocation());
        time_text.setText(String.valueOf(mData.get(position).getTimestamp()));
        return  convertView;
    }
}
