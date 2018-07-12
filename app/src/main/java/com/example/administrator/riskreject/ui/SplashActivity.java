package com.example.administrator.riskreject.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.riskreject.R;
import com.example.administrator.riskreject.bean.SplashInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import me.weyye.hipermission.HiPermission;

public class SplashActivity extends AppCompatActivity {
    private static final int START_ACTIVITY = 0x1;
    private boolean InMainActivity = false;
    ImageView bg;
    //延时自动进入MainActivity
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("InMainActivity = " + InMainActivity);
            //如果InMainActivity == false，则进入MainActivity，为了避免重复进入MainActivity
            if (InMainActivity == false) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case START_ACTIVITY:
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //延时自动进入MainActivity
        handler.sendEmptyMessageDelayed(START_ACTIVITY, 5000);
        bg = findViewById(R.id.bg);
        try {
            File file = new File(takePicRootDir(this) + "/rr_loading.png");
            if (file.exists()) {
                Bitmap bitmap = getLoacalBitmap(file.getAbsolutePath()); //从本地取图片(在cdcard中获取)  //
                bg.setImageBitmap(bitmap); //设置Bitmap
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(HiPermission.checkPermission(this, Manifest.permission.READ_PHONE_STATE)){
            queryData();
        }




    }


    public void jumpHome(View view) {
        InMainActivity = true;
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    /**
     * 查询最新的开屏图 并下载保存本地
     */
    public void queryData() {
        BmobQuery<SplashInfo> query = new BmobQuery<SplashInfo>();
//查询playerName叫“loading”的数据
        query.addWhereEqualTo("name", "loading");
//返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(1);
//执行查询方法
        query.findObjects(new FindListener<SplashInfo>() {
            @Override
            public void done(List<SplashInfo> object, BmobException e) {
                if (e == null) {
                    if (object != null && object.size() != 0) {
                        SplashInfo splashInfo = object.get(0);
                        //下载文件
                        String url = splashInfo.getUrl();
                        BmobFile bmobfile =
                                new BmobFile("rr_loading.png", "", url);
                        File file = new File(takePicRootDir(SplashActivity.this) + "/rr_loading.png");
                        bmobfile.download(file, new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                Log.i("bmob", s);
                            }

                            @Override
                            public void onProgress(Integer integer, long l) {
                                Log.i("bmob", integer + "");
                            }
                        });
//                        bmobfile.download();

                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 判断当前存储卡是否可用
     **/
    public boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取当前需要查询的文件夹
     **/
    public String takePicRootDir(Context context) {
        if (checkSDCardAvailable()) {
            return Environment.getExternalStorageDirectory() + File.separator + "Risk";
        } else {
            return context.getFilesDir().getAbsolutePath() + File.separator + "Risk";
        }
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}