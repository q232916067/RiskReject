package com.example.administrator.riskreject.ui;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.riskreject.R;
import com.suke.widget.SwitchButton;
import com.xdandroid.hellodaemon.IntentWrapper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.update.BmobUpdateAgent;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    EditText editText_phone;
    EditText editText_time;
    EditText editText_msg;

    Button confirm;
    Button jumpSetting;
    Button jumpWhite;

    SwitchButton switchButton;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv);
        editText_phone = ((EditText) findViewById(R.id.et));
        editText_time = ((EditText) findViewById(R.id.et2));
        editText_msg = ((EditText) findViewById(R.id.et3));

        confirm = findViewById(R.id.confirm);
        jumpSetting = findViewById(R.id.jumpSetting);
        jumpWhite = findViewById(R.id.jumpWhite);
        switchButton =
                findViewById(R.id.switch_button);

        confirm.setOnClickListener(this);
        jumpSetting.setOnClickListener(this);
        jumpWhite.setOnClickListener(this);


        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    editor.putBoolean("isCall", true);
                } else {
                    editor.putBoolean("isCall", false);
                }
                editor.apply();
            }
        });

        //     jumpStartInterface(this);
//        Intent intent = new Intent(this, MyService.class);
//        startService(intent);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.riskreject");
        registerReceiver(mBroadcastReceiver, filter);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        String sms = sharedPreferences.getString("sms", "");
        int safe_time = sharedPreferences.getInt("safe_time", 12);
        boolean isCall = sharedPreferences.getBoolean("isCall", false);


        editText_phone.setText(phone);
        editText_time.setText(safe_time + "");
        switchButton.setChecked(isCall);
        editText_msg.setText(sms);

        SmscheckPermission();


        ignoreBatteryOptimization(this);

        int last_ime = sharedPreferences.getInt("time", 0);
        String hh = new DecimalFormat("00").format(last_ime / 3600);
        String mm = new DecimalFormat("00").format(last_ime % 3600 / 60);
        String ss = new DecimalFormat("00").format(last_ime % 60);
        String timeFormat = new String(hh + ":" + mm + ":" + ss);
        textView.setText("你上次操作手机是在" + timeFormat + "前");

        isShowResetDialog();
        //自动更新
        BmobUpdateAgent.update(this);




    }

    private void isShowResetDialog() {
        final SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        boolean isSend = sharedPreferences.getBoolean("isSend", false);
        if (isSend) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("警告！")//设置对话框的标题
                    .setMessage("由于您长时间没有操作手机，已经向您设置的联系人发送了求救信息！")//设置对话框的内容
                    //设置对话框的按钮
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isSend", false);
                            editor.apply();
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
    }

    private void SmscheckPermission() {
        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission.SEND_SMS, "发送短信", R.drawable.permission_ic_sms));
        permissionItems.add(new PermissionItem(Manifest.permission.CALL_PHONE, "拨打电话", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "获取手机状态", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入手机存储", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读取手机存储", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.WAKE_LOCK));
        HiPermission.create(MainActivity.this)
                .title("检测权限")
                .permissions(permissionItems)
                .msg("获取发送短信权限")
                .animStyle(R.style.PermissionAnimScale)
                .style(R.style.PermissionDefaultBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        Log.i(TAG, "onClose");
                        showToast("用户关闭权限申请");
                    }

                    @Override
                    public void onFinish() {
//                        showToast("所有权限申请完成");
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        Log.i(TAG, "onDeny");
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                        Log.i(TAG, "onGuarantee");
                    }
                });
    }

    private static String getMobileType() {
        return Build.MANUFACTURER;
    }

    public static void jumpStartInterface(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("HLQ_Struggle", "******************当前手机型号为：" + getMobileType());
            ComponentName componentName = null;
            if (getMobileType().equals("Xiaomi")) { // 红米Note4测试通过
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (getMobileType().equals("Letv")) { // 乐视2测试通过
                intent.setAction("com.letv.android.permissionautoboot");
            } else if (getMobileType().equals("samsung")) { // 三星Note5测试通过
                componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram.AutoRunActivity");
            } else if (getMobileType().equals("HUAWEI")) { // 华为测试通过
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            } else if (getMobileType().equals("vivo")) { // VIVO测试通过
                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.PurviewTabActivity");
            } else if (getMobileType().equals("Meizu")) { //万恶的魅族
                // 通过测试，发现魅族是真恶心，也是够了，之前版本还能查看到关于设置自启动这一界面，系统更新之后，完全找不到了，心里默默Fuck！
                // 针对魅族，我们只能通过魅族内置手机管家去设置自启动，所以我在这里直接跳转到魅族内置手机管家界面，具体结果请看图
                componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");
            } else if (getMobileType().equals("OPPO")) { // OPPO R8205测试通过
                componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
            } else if (getMobileType().equals("ulong")) { // 360手机 未测试
                componentName = new ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity");
            } else {                // 以上只是市面上主流机型，由于公司你懂的，所以很不容易才凑齐以上设备
                // 针对于其他设备，我们只能调整当前系统app查看详情界面
                // 在此根据用户手机当前版本跳转系统设置界面
                if (Build.VERSION.SDK_INT >= 9) {
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                }
            }
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) //onReceive函数不能做耗时的事情，参考值：10s以内
        {
            Log.d("scott", "on receive action=" + intent.getAction());
            String action = intent.getAction();
            if (action.equals("com.riskreject")) {
//                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
//                int last_ime = sharedPreferences.getInt("time", 0);
//                String hh = new DecimalFormat("00").format(last_ime / 3600);
//                String mm = new DecimalFormat("00").format(last_ime % 3600 / 60);
//                String ss = new DecimalFormat("00").format(last_ime % 60);
//                String timeFormat = new String(hh + ":" + mm + ":" + ss);
//                textView.setText("你上次操作手机是在" + timeFormat + "前");
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);

        super.onDestroy();
    }


    public void btnClick(View view) {
        try {
            String phone = editText_phone.getText().toString();
            String safe_time = editText_time.getText().toString();
            String sms = editText_msg.getText().toString();
            if (TextUtils.isEmpty(phone) || phone.length() < 11) {
                showToast("手机号设定不正确");
                return;
            }
            if (TextUtils.isEmpty(safe_time)) {
                showToast("安全时间不能为空");
                return;
            }
            if (TextUtils.isEmpty(sms)) {
                showToast("请设置短信内容");
                return;
            }
            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("phone", phone);
            edit.putString("sms", sms);
            edit.putInt("safe_time", Integer.parseInt(safe_time));
            edit.apply();
            showToast("设置成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showToast("设置失败");
        }

    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    public void jumpSetting(View view) {
        jumpStartInterface(this);
    }

    /**
     * 忽略电池优化
     */
    public void ignoreBatteryOptimization(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
                /**
                 * 判断当前APP是否有加入电池优化的白名单，
                 * 如果没有，弹出加入电池优化的白名单的设置对话框
                 * */
                if (!hasIgnored) {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
    }

    //防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
    public void onBackPressed() {
        IntentWrapper.onBackPressed(this);
    }

    public void jumpWhite(View view) {
        IntentWrapper.whiteListMatters(this, "监听手机服务持续运行");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.confirm:
                btnClick(v);
                break;
            case R.id.jumpSetting:
                jumpSetting(v);
                break;
            case R.id.jumpWhite:
                jumpWhite(v);
                break;
        }
    }
}
