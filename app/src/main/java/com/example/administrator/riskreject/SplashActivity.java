package com.example.administrator.riskreject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SplashActivity extends AppCompatActivity {
    private static final int START_ACTIVITY = 0x1;
    private boolean InMainActivity = false;
    //延时自动进入MainActivity
    private Handler handler = new Handler(){
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

        }

    public void jumpHome(View view) {
        InMainActivity=true;
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}