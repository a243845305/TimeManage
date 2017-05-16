package com.timemanage.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.timemanage.utils.LogUtil;

import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * 想用计时器的方式来实现每日更新操作，但是并没有成功，这段代码没有使用，但还是留着也许以后会有新的更改。
 * Created by Yawen_Li on 2017/5/16.
 */
public class DateChangeserve {
    private static String TAG = "TimeChangeserver";
    private DateStateListener mDateStateListener;
    private DateStateBroadcastReceiver mDateStateBroadcastReceiver;
    private Context mContext;
    private Calendar mCalendar;
    private boolean firstStart;

    public DateChangeserve(Context context){
        mContext = context;
        mDateStateBroadcastReceiver = new DateStateBroadcastReceiver();
    }

    public void requestScreenStateUpdate(DateStateListener listener) {
        mDateStateListener = listener;
        startRemind(mContext);
    }


    public class DateStateBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("DateStateBroadcastReceiver"))
            LogUtil.e("Now   DateChangeServe","onReceive");
            mDateStateListener.onDateTimeToInsert();
        }
    }

    //启动定时任务计时器
    private void startRemind(Context context) {

        //得到日历实例，主要是为了下面的获取时间
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒 设置的为13点
        mCalendar.set(Calendar.HOUR_OF_DAY, 15);
        //设置在几分提醒 设置的为25分
        mCalendar.set(Calendar.MINUTE, 12);


        //上面设置的就是13点25分的时间点

        //获取上面设置的13点25分的毫秒值
        long selectTime = mCalendar.getTimeInMillis();

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //DateStateBroadcastReceiver.class为广播接受者
        IntentFilter filter = new IntentFilter();
        filter.addAction("DateStateBroadcastReceiver");
        mContext.registerReceiver(mDateStateBroadcastReceiver, filter);

        Intent intent = new Intent();
        intent.setAction("DateStateBroadcastReceiver");
        intent.setClass(context,DateStateBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        /**
         * * 重复提醒 *
         * 第一个参数是警报类型；
         * 第二个参数网上说法不一，
         * 第三个参数是重复周期，也就是下次提醒的间隔 毫秒值 我这里是一天后提醒
         * (1000 * 60 * 60 * 24)
         */
        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 200000, pi);

    }

    public interface DateStateListener {
        public void onDateTimeToInsert();
    }
}
