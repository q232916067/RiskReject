package com.example.administrator.riskreject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.xdandroid.hellodaemon.AbsWorkService;

public class MyService extends AbsWorkService {
    BroadcastReceiver mBatInfoReceiver;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            String phone = sharedPreferences.getString("phone", "");
            String sms = sharedPreferences.getString("sms", "我遇到危险了，快来救我！！！");
            boolean isSend = sharedPreferences.getBoolean("isSend", false);
            boolean isCall = sharedPreferences.getBoolean("isCall", false);
            switch (msg.what) {
                case 0:
                    Intent intent1 = new Intent("com.riskreject");

                    int time = sharedPreferences.getInt("time", 0);

                    time++;
                    Log.e("lzp", "timer excute==" + time);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("time", time);
                    editor.commit();

                    intent1.putExtra("time", time);
                    sendBroadcast(intent1);


                    int safe_time = sharedPreferences.getInt("safe_time", 12);

                    if (time / 3600 > safe_time) {
                        if (!TextUtils.isEmpty(phone) && !isSend) {

                            editor.putBoolean("isSend", true);
                            editor.commit();
                            handler.sendEmptyMessageDelayed(1, 1000);
                            handler.sendEmptyMessageDelayed(1, 2000);
                            if(isCall){
                                handler.sendEmptyMessageDelayed(2, 5000);
                            }

                        }
                    }
                    break;
                case 1:
                    SMSMethod.getInstance(getApplicationContext()).SendMessage(phone, sms);
                    break;
                case 2:
                    Intent intent = new Intent();
                    intent.setAction("Android.intent.action.CALL");
                    //intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);//方法内部自动添加android.intent.category.DEFAULT
                    break;
            }


            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    public MyService() {
    }

    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return false;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {

    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        cancelJobAlarmSub();
        SMSMethod.getInstance(this).unregisterReceiver();
        unregisterReceiver(mBatInfoReceiver);
    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        return true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {

    }


    @Override
    public void onCreate() {
        super.onCreate();

        SMSMethod.getInstance(this).registerReceiver();
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                Log.d("", "onReceive");
                String action = intent.getAction();

                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    Log.d("MyService", "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.d("MyService", "screen off");
                    SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("time", 0);
                    editor.commit();
                    handler.sendEmptyMessageDelayed(0, 1000);
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    Log.d("MyService", "screen unlock");
                    handler.removeMessages(0);

                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    Log.i("MyService", " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                }
            }
        };
        Log.d("MyService", "registerReceiver");
        registerReceiver(mBatInfoReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("MyService", "onDestroy");
        super.onDestroy();
    }
}
