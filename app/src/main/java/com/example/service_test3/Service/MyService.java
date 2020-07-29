package com.example.service_test3.Service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.service_test3.R;
import com.example.service_test3.activity.MainActivity;
import com.example.service_test3.bluetooth.BlueTooth;
import com.example.service_test3.bluetooth.BluetoothReceiver;
import com.example.service_test3.db.ModaiDB;

public class MyService extends Service {
    private ModaiDB modaiDB;
    AlarmManager manager;//alarm管理器
    private BluetoothReceiver receiver;
    BlueTooth blueTooth ;
    int i = 0;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        modaiDB = ModaiDB.getInstance(this);
        blueTooth = new BlueTooth();
        //创建前台服务
        String CHANNEL_ID = "com.example.service";
        String CHANNEL_NAME = "魔带";
        NotificationChannel notificationChannel = null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID).
//                setContentTitle("This is content title").
//                setContentText("This is content text").
//                setWhen(System.currentTimeMillis()).
                  setSmallIcon(R.mipmap.ic_launcher).
//                setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)).
        setContentIntent(pendingIntent).build();
        startForeground(1, notification);

        registerBluetooth();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Scan","ready");
                blueTooth.startScan();
                if(i == 3) {
                    modaiDB.Show_Bluetoothinfo();
                }
                i++;
            }
        }).start();
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 300* 1000;  // 这是一小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent alarmIntent = new Intent(this,MyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//  4.4
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime, pendingIntent);
        } else {
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime, pendingIntent);
        }
//        ChargeManager.releaseCpuLock();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    private void registerBluetooth(){
        // 设置广播信息过滤
        receiver = new BluetoothReceiver(modaiDB);
        IntentFilter bluetooth_intentFilter = new IntentFilter();
        bluetooth_intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        bluetooth_intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        bluetooth_intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        bluetooth_intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        bluetooth_intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //注册广播
        registerReceiver(receiver, bluetooth_intentFilter);
    }

}
