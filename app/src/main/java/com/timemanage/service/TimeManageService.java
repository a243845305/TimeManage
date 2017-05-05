package com.timemanage.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.timemanage.R;
import com.timemanage.bean.AppInfo;
import com.timemanage.utils.ApkUtil;
import com.timemanage.utils.ConstantUtil;
import com.timemanage.utils.LogUtil;
import com.timemanage.view.activity.MainActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * 前台Service 对各应用使用时长进行监控
 * Created by Yawen_Li on 2017/1/16.
 */
public class TimeManageService extends MyIntentService {

    private ScreenObserve screenObserve;
    private boolean isScreenOn;

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
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Foreground Service Start");
        builder.setContentTitle("Foreground Service");
        builder.setContentText("Make this service run in the foreground.");
        Notification notification = builder.build();
        startForeground(1, notification);

        isScreenOn = true;

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
                final List<AppInfo> appInfos = ApkUtil.scanLocalInstallAppList(TimeManageService.this.getPackageManager());
                int count = 0;
                for (AppInfo appInfo: appInfos ) {
                    LogUtil.e(count+"appName",appInfo.getAppName());
                    count++;
                }

                String foregroundProcess = ApkUtil.getForegroundApp(TimeManageService.this.getPackageManager());
                LogUtil.e("foregroundProcess:",foregroundProcess);
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
            if (!(foregroundProcess == null))
                LogUtil.e("foregroundProcess:",foregroundProcess);
            try {
                Thread.sleep(5000);
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
