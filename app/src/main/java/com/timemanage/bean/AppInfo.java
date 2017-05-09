package com.timemanage.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Yawen_Li on 2017/5/5.
 */
public class AppInfo {
    private Drawable appIcon;
    private String appName;
    private String appPackageName;
    private String appDuration;

    public AppInfo(Drawable appIcon, String appName, String appDuration, String appPackageName) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.appDuration = appDuration;
        this.appPackageName = appPackageName;
    }
    public AppInfo() {

    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable image) {
        this.appIcon = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppDuration(String appDuration){
        this.appDuration = appDuration;
    }

    public String getAppDuration(){
        return appDuration;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }
}
