package com.example.administrator.riskreject;

import android.app.Application;

import com.xdandroid.hellodaemon.DaemonEnv;

/**
 * Created by Administrator on 2018/7/7.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DaemonEnv.initialize(
                this,  //Application Context.
                MyService.class, //刚才创建的 Service 对应的 Class 对象.
                6);  //定时唤醒的时间间隔(ms), 默认 6 分钟.
        DaemonEnv.startServiceMayBind(MyService.class);
    }
}
