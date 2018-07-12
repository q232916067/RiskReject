package com.example.administrator.riskreject;

import android.app.Application;
import android.util.Log;

import com.xdandroid.hellodaemon.DaemonEnv;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;

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

        //第一：默认初始化
        Bmob.initialize(this, "b5608447e36f33a381c7ed2612c0554f");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

//        BmobUpdateAgent.initAppVersion();
        // 使用推送服务时的初始化操作
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    Log.d("MyApplication", (bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId()));
                } else {
                    Log.d("MyApplication", e.getMessage());
                }
            }
        });
// 启动推送服务
        BmobPush.startWork(this);
    }
}
