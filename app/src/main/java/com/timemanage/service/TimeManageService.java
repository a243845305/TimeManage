package com.timemanage.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.timemanage.R;
import com.timemanage.utils.LogUtil;
import com.timemanage.view.activity.MainActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * 前台Service 对各应用使用时长进行监控
 * Created by Yawen_Li on 2017/1/16.
 */
public class TimeManageService extends Service {

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


//    private static final String TAG = "TimeManageService";
//
//    private boolean mReflectFlg = false;
//
//    private static final int NOTIFICATION_ID = 1; // 如果id设置为0,会导致不能设置为前台service
//    private static final Class<?>[] mSetForegroundSignature = new Class[] {
//            boolean.class};
//    private static final Class<?>[] mStartForegroundSignature = new Class[] {
//            int.class, Notification.class};
//    private static final Class<?>[] mStopForegroundSignature = new Class[] {
//            boolean.class};
//
//    private NotificationManager mNM;
//    private Method mSetForeground;
//    private Method mStartForeground;
//    private Method mStopForeground;
//    private Object[] mSetForegroundArgs = new Object[1];
//    private Object[] mStartForegroundArgs = new Object[2];
//    private Object[] mStopForegroundArgs = new Object[1];
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        LogUtil.d(TAG, "onCreate");
//        LogUtil.e("TMS", "tmss");
//        mNM = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        try {
//            mStartForeground = TimeManageService.class.getMethod("startForeground", mStartForegroundSignature);
//            mStopForeground = TimeManageService.class.getMethod("stopForeground", mStopForegroundSignature);
//        } catch (NoSuchMethodException e) {
//            mStartForeground = mStopForeground = null;
//        }
//
//        try {
//            mSetForeground = getClass().getMethod("setForeground",
//                    mSetForegroundSignature);
//        } catch (NoSuchMethodException e) {
//            throw new IllegalStateException(
//                    "OS doesn't have Service.startForeground OR Service.setForeground!");
//        }
//
//        Notification.Builder builder = new Notification.Builder(this);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), 0);
//        builder.setContentIntent(contentIntent);
////        builder.setSmallIcon(R.drawable.icon);
//        builder.setTicker("Foreground Service Start");
//        builder.setContentTitle("Foreground Service");
//        builder.setContentText("Make this service run in the foreground.");
//        Notification notification = builder.build();
//
//        startForegroundCompat(NOTIFICATION_ID, notification);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        LogUtil.d(TAG, "onStartCommand");
//
//        return START_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        LogUtil.d(TAG, "onDestroy");
//
//        stopForegroundCompat(NOTIFICATION_ID);
//    }
//
//    void invokeMethod(Method method, Object[] args) {
//        try {
//            method.invoke(this, args);
//        } catch (InvocationTargetException e) {
//            // Should not happen.
//            Log.w("ApiDemos", "Unable to invoke method", e);
//        } catch (IllegalAccessException e) {
//            // Should not happen.
//            Log.w("ApiDemos", "Unable to invoke method", e);
//        }
//    }
//
//    /**
//     * This is a wrapper around the new startForeground method, using the older
//     * APIs if it is not available.
//     */
//    void startForegroundCompat(int id, Notification notification) {
//        if (mReflectFlg) {
//            // If we have the new startForeground API, then use it.
//            if (mStartForeground != null) {
//                mStartForegroundArgs[0] = Integer.valueOf(id);
//                mStartForegroundArgs[1] = notification;
//                invokeMethod(mStartForeground, mStartForegroundArgs);
//                return;
//            }
//
//            // Fall back on the old API.
//            mSetForegroundArgs[0] = Boolean.TRUE;
//            invokeMethod(mSetForeground, mSetForegroundArgs);
//            mNM.notify(id, notification);
//        } else {
//            /* 还可以使用以下方法，当sdk大于等于5时，调用sdk现有的方法startForeground设置前台运行，
//             * 否则调用反射取得的sdk level 5（对应Android 2.0）以下才有的旧方法setForeground设置前台运行 */
//
//            if(Build.VERSION.SDK_INT >= 5) {
//                startForeground(id, notification);
//            } else {
//                // Fall back on the old API.
//                mSetForegroundArgs[0] = Boolean.TRUE;
//                invokeMethod(mSetForeground, mSetForegroundArgs);
//                mNM.notify(id, notification);
//            }
//        }
//    }
//
//    /**
//     * This is a wrapper around the new stopForeground method, using the older
//     * APIs if it is not available.
//     */
//    void stopForegroundCompat(int id) {
//        if (mReflectFlg) {
//            // If we have the new stopForeground API, then use it.
//            if (mStopForeground != null) {
//                mStopForegroundArgs[0] = Boolean.TRUE;
//                invokeMethod(mStopForeground, mStopForegroundArgs);
//                return;
//            }
//
//            // Fall back on the old API.  Note to cancel BEFORE changing the
//            // foreground state, since we could be killed at that point.
//            mNM.cancel(id);
//            mSetForegroundArgs[0] = Boolean.FALSE;
//            invokeMethod(mSetForeground, mSetForegroundArgs);
//        } else {
//            /* 还可以使用以下方法，当sdk大于等于5时，调用sdk现有的方法stopForeground停止前台运行，
//             * 否则调用反射取得的sdk level 5（对应Android 2.0）以下才有的旧方法setForeground停止前台运行 */
//
//            if(Build.VERSION.SDK_INT >= 5) {
//                stopForeground(true);
//            } else {
//                // Fall back on the old API.  Note to cancel BEFORE changing the
//                // foreground state, since we could be killed at that point.
//                mNM.cancel(id);
//                mSetForegroundArgs[0] = Boolean.FALSE;
//                invokeMethod(mSetForeground, mSetForegroundArgs);
//            }
//        }
//    }
}
