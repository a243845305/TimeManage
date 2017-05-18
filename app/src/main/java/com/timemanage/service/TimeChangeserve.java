package com.timemanage.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import com.timemanage.utils.ConstantUtil;
import com.timemanage.utils.LogUtil;

import java.util.Calendar;

/**
 *
 * 监控时间变化的serve类，由于无法在锁屏状态下监控到时间的变化，所以在换日期插入操作上使用了一个并不稳定的一个方式
 * Created by Yawen_Li on 2017/5/12.
 */

public class TimeChangeserve {
    private static String TAG = "TimeChangeserver";
    private TimeStateListener mTimeStateListener;
    private TimeStateBroadcastReceiver mTimeStateBroadcastReceiver;
    private Context mContext;
    private Calendar c;
    private boolean firstStart;
    //控制每天插入次数的一个变量
    public static int count;

    public TimeChangeserve(Context context) {
        mContext = context;
        mTimeStateBroadcastReceiver = new TimeStateBroadcastReceiver();
        firstStart = true;
    }


    public class TimeStateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_TIME_TICK.equals(action)){

                c = Calendar.getInstance();
                int minute = c.get(Calendar.MINUTE);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                LogUtil.e("收到广播TIME_TICK Now hour:",hour+"  minute:"+minute);
                //用这样的一种方式来实现每天更新插入的操作，希望能有效果吧
                if (hour == 0 && minute < 10 && count < 1) {
                    count = 1;
                    mTimeStateListener.onTimeToInsert();
                }else if (minute == 56){
                    mTimeStateListener.onTimeToUpdate();
                }
            }

        }
    }

    public void requestScreenStateUpdate(TimeStateListener listener) {
        mTimeStateListener = listener;
        startTimeStateBroadcastReceiver();

        firstStartReceiver();
    }

    private void firstStartReceiver(){
        if (firstStart){
            firstStart = false;
            mTimeStateListener.onTimeReceiverFirstStart();
        }
    }

    private void startTimeStateBroadcastReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        mContext.registerReceiver(mTimeStateBroadcastReceiver, filter);
    }

    public interface TimeStateListener {
        public void onTimeToInsert();
        public void onTimeReceiverFirstStart();
        public void onTimeToUpdate();
    }
}
