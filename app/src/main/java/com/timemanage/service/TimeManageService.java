package com.timemanage.service;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
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
 *
 * 前台Service 对各应用使用时长进行监控
 * Created by Yawen_Li on 2017/1/16.
 */
public class TimeManageService extends MyIntentService {

    private ScreenObserve screenObserve;
    private boolean isScreenOn;
    private DataBaseManager dbManager;
    private List<AppInfo> appInfos;
    private Time t;
    private Calendar c;
    private Date date;

    public TimeManageService() {
        super("TimeManageService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e("LogService","create");
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

        date = new Date();
        t = new Time("GMT+8");
        isScreenOn = true;
        dbManager = new DataBaseManager(this);

//        DataBaseHelper dbHelper = new DataBaseHelper(this);
//        dbHelper.deleteDBByName();

        screenObserve = new ScreenObserve(TimeManageService.this);
        screenObserve.requestScreenStateUpdate(new ScreenObserve.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                //每次屏幕唤醒后将服务重启，即可再次执行监听事件
                isScreenOn = true;
                ComponentName componentName = new ComponentName(ConstantUtil.pkgName,ConstantUtil.serviceName);
                Intent intent = new Intent();
                intent.setComponent(componentName);
                startService(intent);
            }

            @Override
            public void onScreenOff() {
                isScreenOn = false;
            }
        });

        new Thread() {
            @Override
            public void run() {
                super.run();
                appInfos = ApkUtil.scanLocalInstallAppList(TimeManageService.this.getPackageManager());
                int count = 0;
                for (AppInfo appInfo: appInfos ) {
                    LogUtil.e(count+"appName",appInfo.getAppName());
                    LogUtil.e(count+"appIcon",appInfo.getAppIcon().toString());
                    LogUtil.e(count+"appPackageName",appInfo.getAppPackageName());
                    appInfo.setAppDuration("0");
                    count++;
                }
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

        while(isScreenOn){
            String foregroundProcess = ApkUtil.getForegroundApp(TimeManageService.this.getPackageManager());
            if (foregroundProcess != null){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (AppInfo appInfo: appInfos){
                    if (foregroundProcess.equals(appInfo.getAppPackageName())){
                        int d = Integer.parseInt(appInfo.getAppDuration());
                        d = d+1;
                        appInfo.setAppDuration(d+"");
                        LogUtil.e("appDuration",appInfo.getAppDuration());
                    }
                }
                LogUtil.e("foregroundProcess:",foregroundProcess);
            }
            c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int minute = c.get(Calendar.MINUTE);
            int hour =  c.get(Calendar.HOUR_OF_DAY);
            LogUtil.e("Now minute:::::",minute+""+"hour:::"+hour+"   month::::"+month+"   day:::"+day+"   year:::"+year);
            if (hour == 21 && minute == 16){
                //每到零点时进行插入数据操作
                //月份需要加1
                dbManager.insertAppDurationTot_apptime(appInfos,year,month+1,day);
            }

            if (minute == 0){
                //每到整点时进行更新数据操作
                dbManager.updateAppDurationTot_apptime(appInfos,year,month,day);
            }

            try {
                Thread.sleep(6*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException("interrupted",e);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
        ComponentName componentName = new ComponentName(ConstantUtil.pkgName,ConstantUtil.serviceName);
        Intent intent = new Intent();
        intent.setComponent(componentName);
        startService(intent);
    }
}
