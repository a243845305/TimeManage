package com.timemanage;

import android.app.Application;
import android.content.Context;


/**
 * 全局的application
 * Created by Yawen_Li on 2017/5/9.
 */
public class TimeManageAppliaction extends Application {
    private static Context context;

    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }

    public static Context getContext() {
        return context;
    }
}
