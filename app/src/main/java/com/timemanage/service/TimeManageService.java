package com.timemanage.service;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.format.Time;

import com.timemanage.R;
import com.timemanage.TimeManageAppliaction;
import com.timemanage.bean.AppInfo;
import com.timemanage.db.DataBaseHelper;
import com.timemanage.db.DataBaseManager;
import com.timemanage.utils.ApkUtil;
import com.timemanage.utils.ConstantUtil;
import com.timemanage.utils.LogUtil;
import com.timemanage.view.activity.MainActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 前台Service 对各应用使用时长进行监控
 * Created by Yawen_Li on 2017/1/16.
 */
public class TimeManageService extends MyIntentService {

    private static String TAG = "TimeManageService";
    private ScreenObserve screenObserve;
    private TimeChangeserve timeChangeserve;
    private boolean isScreenOn;
    private DataBaseManager dbManager;
    private List<AppInfo> appInfos;
    private Calendar c;

    public TimeManageService() {
        super("TimeManageService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e("LogService", "create");
        Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.img_appicon_64px);
        builder.setTicker("Foreground Service Start");
        builder.setContentTitle("TimeManage Service");
        builder.setContentText("TimeManage正在为您提供服务");
        Notification notification = builder.build();
        startForeground(1, notification);

        isScreenOn = true;
        dbManager = new DataBaseManager(this);
        timeChangeserve = new TimeChangeserve(TimeManageService.this);
        screenObserve = new ScreenObserve(TimeManageService.this);


        //控制手机cpu不休眠
        PowerManager powerManager =(PowerManager)getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,TAG);//设置休眠状态
        //锁定休眠状态
        wakeLock.acquire();

//        DataBaseHelper dbHelper = new DataBaseHelper(this);
//        dbHelper.deleteDBByName();

        new Thread() {
            @Override
            public void run() {
                super.run();
                appInfos = ApkUtil.scanLocalInstallAppList(TimeManageService.this.getPackageManager());
                int count = 0;
                for (AppInfo appInfo : appInfos) {
                    LogUtil.e(count + "appName", appInfo.getAppName());
                    LogUtil.e(count + "appIcon", appInfo.getAppIcon().toString());
                    LogUtil.e(count + "appPackageName", appInfo.getAppPackageName());
                    appInfo.setAppDuration("0");
                    count++;
                }
                //扫描所有app并写入app列表中
                dbManager.insertAppContentsTot_app(appInfos);
            }
        }.start();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        handleBroadcastIntent(intent);
    }

    @Override
    protected void handleBroadcastIntent(Intent broadcastIntent) {

        while (isScreenOn) {
            String foregroundProcess = ApkUtil.getForegroundApp(TimeManageService.this.getPackageManager());
            if (foregroundProcess != null) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (AppInfo appInfo : appInfos) {
                    if (foregroundProcess.equals(appInfo.getAppPackageName())) {
                        int d = 0;
                        if (appInfo.getAppDuration() != null){
                            d = Integer.parseInt(appInfo.getAppDuration());
                        }else {
                            appInfo.setAppDuration(0+"");
                        }
                        d = d + 1;
                        appInfo.setAppDuration(d + "");
                        LogUtil.e("appDuration", appInfo.getAppDuration());
                    }
                }
                LogUtil.e("foregroundProcess:", foregroundProcess);
            }
            c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int minute = c.get(Calendar.MINUTE);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            LogUtil.e("Now minute:::::", minute + "" + "hour:::" + hour + "   month::::" + month + "   day:::" + day + "   year:::" + year);


            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException("interrupted", e);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        timeChangeserve.requestScreenStateUpdate(new TimeChangeserve.TimeStateListener() {
            @Override
            public void onTimeToInsert() {
                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                LogUtil.e("该新插入数据了Now ","   month::::" + month + "   day:::" + day + "   year:::" + year);
//                每到零点时进行插入数据操作
//                月份需要加1
                dbManager.insertAppDurationTot_apptime(appInfos, year, month + 1, day);
            }

            @Override
            public void onTimeReceiverFirstStart() {
                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                appInfos = ApkUtil.scanLocalInstallAppList(TimeManageService.this.getPackageManager());
                LogUtil.e("Now ","   month::::" + month + "   day:::" + day + "   year:::" + year);
                //首次启动需要新增数据
                //月份需要加1
                dbManager.insertAppDurationTot_apptime(appInfos, year, month + 1, day);
            }

            @Override
            public void onTimeToUpdate() {
                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                TimeChangeserve.count = 0;
                appInfos = ApkUtil.scanLocalInstallAppList(TimeManageService.this.getPackageManager());
                LogUtil.e("该更新了Now ","   month::::" + month + "   day:::" + day + "   year:::" + year);
                //每到整点需要更新数据
                //月份需要加1
                dbManager.updateAppDurationTot_apptime(appInfos, year, month+1, day);
            }
        });

        screenObserve.requestScreenStateUpdate(new ScreenObserve.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                //每次屏幕唤醒后将服务重启，即可再次执行监听事件
                isScreenOn = true;
//                ComponentName componentName = new ComponentName(ConstantUtil.pkgName, ConstantUtil.serviceName);
//                Intent intent = new Intent();
//                intent.setComponent(componentName);
//                startService(intent);
            }

            @Override
            public void onScreenOff() {
                isScreenOn = false;
            }
        });

        LogUtil.e("TimeService","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //重启服务
        ComponentName componentName = new ComponentName(ConstantUtil.pkgName, ConstantUtil.serviceName);
        Intent intent = new Intent();
        intent.setComponent(componentName);
        startService(intent);
    }
}
