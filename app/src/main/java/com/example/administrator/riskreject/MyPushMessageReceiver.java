package com.example.administrator.riskreject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.administrator.riskreject.bean.PushInfo;
import com.example.administrator.riskreject.ui.MainActivity;
import com.google.gson.Gson;

import cn.bmob.newim.notification.NotificationCompat;
import cn.bmob.push.PushConstants;

/**
 * Created by Administrator on 2018/7/12.
 */

//TODO 集成：1.3、创建自定义的推送消息接收器，并在清单文件中注册
public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Log.d("bmob", "客户端收到推送内容：" + intent.getStringExtra("msg"));

            String msgObj = intent.getStringExtra("msg");
            Gson gson = new Gson();

            PushInfo pushInfo = gson.fromJson(msgObj, PushInfo.class);

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pi = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setTicker(pushInfo.getTicker())
                    .setContentTitle(pushInfo.getTitle())
                    .setContentText(pushInfo.getContentText())
                    .setContentIntent(pi)
                    .setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = builder.build();
            notificationManager.notify(0, notification);
        }
    }

}