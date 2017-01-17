package com.timemanage.service;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.timemanage.view.activity.MainActivity;

/**
 *
 * 前台Service 对各应用使用时长进行监控
 * Created by Yawen_Li on 2017/1/16.
 */
public class TimeManageService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("")
                .setContentText("")
                .setContentIntent(pendingIntent)
//                .setSmallIcon()
                .build();

        startForeground(1001,notification);

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
    }
}
