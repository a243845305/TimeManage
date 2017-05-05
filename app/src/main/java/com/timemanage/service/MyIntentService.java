package com.timemanage.service;

/**
 *
 * 自定义Service，可进行耗时操作
 * Created by Yawen_Li on 2017/5/5.
 */
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public abstract class MyIntentService extends Service {

    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    private String mName;

    private final class ServiceHandler extends Handler{
        public ServiceHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onHandleIntent((Intent)msg.obj);
        }
    }

    public MyIntentService(String name){
        super();
        mName = name;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("IntentService["+mName+"]");
        thread.start();
        mServiceLooper= thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }
    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        mServiceHandler.sendMessage(msg);
    }

    protected abstract void onHandleIntent(Intent intent);

    protected abstract void handleBroadcastIntent(Intent broadcastIntent);

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public void onDestroy() {
    }



}
