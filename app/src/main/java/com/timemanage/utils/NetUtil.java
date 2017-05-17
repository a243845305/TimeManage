package com.timemanage.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * 判断网路状态的Util
 *
 * @author 高海龙
 */
public class NetUtil {

    /**
     * 判断网络是否可用
     *
     * @param context 上下文
     */
    public static boolean isNetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
}

    /**
     * 外部调用的方法（尽量使用上面的方法）
     *
     * @param activity 当前的activity
     */
    public static void setNetState(final Activity activity) {
        if (NetUtil.isNetAvailable(activity)) {
            AlertDialog.Builder abBuilder = new AlertDialog.Builder(activity);
            abBuilder.setTitle("提示").setMessage("无可用网络，请立即设置").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);
                }
            }).show();
        }
    }
}
