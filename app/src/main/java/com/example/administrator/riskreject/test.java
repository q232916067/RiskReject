//package com.example.administrator.riskreject;
//
//import android.app.Activity;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class test extends Activity implements SensorEventListener {
//    private SensorManager sm;
//    TextView mtextViewx;
//    TextView mtextViewy;
//    TextView mtextViewz;
//    TextView tv;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//        //获取传感器SensorManager对象
//        StringBuffer stringBuffer = new StringBuffer();
//        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
//        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);
//        for (Sensor sensor : sensors) {
//
//            stringBuffer.append(sensor.getName() + "\n");
//
//            Log.v("main",stringBuffer.toString());
//
//
//        }
//        mtextViewx= findViewById(R.id.tv);
//        mtextViewy= findViewById(R.id.tv2);
//        mtextViewz= findViewById(R.id.tv3);
//        tv= findViewById(R.id.tv4);
//      //  tv.setText(stringBuffer);
//
//        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//
//    private long lastUpdateTime;
//    private float lastX;
//    private float lastY;
//    private float lastZ;
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            float X_lateral = sensorEvent.values[0];
//            float Y_longitudinal = sensorEvent.values[1];
//            float Z_vertical = sensorEvent.values[2];
//            mtextViewx.setText(X_lateral + "");
//            mtextViewy.setText(Y_longitudinal + "");
//            mtextViewz.setText(Z_vertical + "");
//            long currentUpdateTime = System.currentTimeMillis();
//            lastUpdateTime = currentUpdateTime;
//            float[] values = sensorEvent.values;
//            long timeInterval = currentUpdateTime - lastUpdateTime;
//            // 获得x,y,z加速度
//            float x = values[0];
//            float y = values[1];
//            float z = values[2];
//
//            // 获得x,y,z加速度的变化值
//            float deltaX = x - lastX;
//            float deltaY = y - lastY;
//            float deltaZ = z - lastZ;
//
//            // 将现在的坐标变成last坐标
//            lastX = x;
//            lastY = y;
//            lastZ = z;
//
//
//            double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
//                    * deltaZ)
//                    / timeInterval * 10000;
//         tv.setText(speed+"速度");
//
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//}
