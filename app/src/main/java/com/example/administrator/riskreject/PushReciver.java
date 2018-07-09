package com.example.administrator.riskreject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/7/7.
 */


public class PushReciver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
//        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
//            Log.d("PushReciver", "开机启动");
//        }else if(Intent.ACTION_USER_PRESENT.equals(intent.getAction())){
//            Log.d("PushReciver", "手机被唤醒");
//            Intent i = new Intent();
//            i.setClass(context, MyService.class);
//            context.startService(i);
//        }else if("com.example.service_destory".equals(intent.getAction())){
//            Log.d("PushReciver", "上次服务被挂了");
//            Intent i = new Intent();
//            i.setClass(context, MyService.class);
//        }else if("com.example.clock".equals(intent.getAction())){
//            Log.d("PushReciver", "定时闹钟的广播");
//            Intent i = new Intent();
//            i.setClass(context, MyService.class);
//            context.startService(i);
//        }
    }
}
