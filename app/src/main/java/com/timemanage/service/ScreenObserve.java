package com.timemanage.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import com.timemanage.utils.LogUtil;

import java.lang.reflect.Method;

/**
 * 对屏幕是否锁屏进行监听
 * Created by Yawen_Li on 2017/5/5.
 */
public class ScreenObserve{
    private static String TAG = "ScreenObserver";
    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;
    private static Method mReflectScreenState;

    public ScreenObserve(Context context){
        mContext = context;
        mScreenReceiver = new ScreenBroadcastReceiver();
        try {
            mReflectScreenState = PowerManager.class.getMethod("isScreenOn",
                    new Class[] {});
        } catch (NoSuchMethodException nsme) {
            LogUtil.d(TAG, "API < 7," + nsme);
        }
    }

    /**
     * screen状态广播接收者
     *
     */
    public class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;
        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if(Intent.ACTION_SCREEN_ON.equals(action)){
                LogUtil.e("ScreenReceiver","ScreenOn");
                mScreenStateListener.onScreenOn();
            }else if(Intent.ACTION_SCREEN_OFF.equals(action)){
                LogUtil.e("ScreenReceiver","ScreenOff");
                mScreenStateListener.onScreenOff();
            }
        }
    }


    /**
     * 请求screen状态更新
     * @param listener
     */
    public void requestScreenStateUpdate(ScreenStateListener listener) {
        mScreenStateListener = listener;
        startScreenBroadcastReceiver();

        firstGetScreenState();
    }

    /**
     * 第一次请求screen状态
     */
    private void firstGetScreenState(){
        PowerManager manager = (PowerManager) mContext
                .getSystemService(Activity.POWER_SERVICE);
        if (isScreenOn(manager)) {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOn();
            }
        } else {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOff();
            }
        }
    }

    /**
     * 停止screen状态更新
     */
    public void stopScreenStateUpdate(){
        mContext.unregisterReceiver(mScreenReceiver);
    }

    /**
     * 启动screen状态广播接收器
     */
    private void startScreenBroadcastReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mContext.registerReceiver(mScreenReceiver, filter);
    }

    /**
     * screen是否打开状态
     * @param pm
     * @return
     */
    private static boolean isScreenOn(PowerManager pm) {
        boolean screenState;
        try {
            screenState = (Boolean) mReflectScreenState.invoke(pm);
        } catch (Exception e) {
            screenState = false;
        }
        return screenState;
    }

    public interface ScreenStateListener {
        public void onScreenOn();
        public void onScreenOff();
    }
}