package com.timemanage.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Yawen_Li on 2017/5/5.
 */
public class AppInfo {
    private Drawable image;
    private String appName;

    public AppInfo(Drawable image, String appName) {
        this.image = image;
        this.appName = appName;
    }
    public AppInfo() {

    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
