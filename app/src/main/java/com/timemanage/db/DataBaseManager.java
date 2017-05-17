package com.timemanage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.timemanage.TimeManageAppliaction;
import com.timemanage.bean.AppInfo;
import com.timemanage.bean.User;
import com.timemanage.utils.LogUtil;
import com.timemanage.utils.UserInfoUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Yawen_Li on 2017/5/6.
 */
public class DataBaseManager {

    String TAG = "DataBaseManager";
    private DataBaseHelper helper;
    private SQLiteDatabase db;
//    private static final DataBaseManager dbManager = new DataBaseManager(TimeManageAppliaction.getContext());
//
//    public static DataBaseManager getDbManager(){
//        return dbManager;
//    }

    public DataBaseManager(Context context) {

        helper = new DataBaseHelper(context);
        db = helper.getWritableDatabase();

    }


    //---------------------------------t_user-----------------------------
    public User findUserByUNameandPwd(String username, String password) {
        db.beginTransaction();// 开始事务
        User user = new User();
        Cursor cursor = null;
        try {
            String sql = "select * from t_user where username = '" + username + "' and password = '" + password + "';";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                user.setUserId(cursor.getInt(cursor.getColumnIndex("userid")));
                user.setUserName(cursor.getString(cursor.getColumnIndex("username")));
                user.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("userid"))));
                user.setUserImg(cursor.getString(cursor.getColumnIndex("userimg")));
            }
            //        byte[] image = cursor.getBlob(cursor.getColumnIndex("userimg"));
            //        ByteArrayInputStream bais = new ByteArrayInputStream(image);
            //        Drawable userImg = Drawable.createFromStream(bais, "imageflag");
            db.setTransactionSuccessful();// 事务成功
        } finally {
            cursor.close();
            db.endTransaction();// 结束事务
        }
        return user;
    }

    public boolean updateUserInfo(User user) {
        boolean isSuccess = false;
        db.beginTransaction();// 开始事务
        try {
            ContentValues cv = new ContentValues();

            cv.put("usernickname", user.getNickName());
            cv.put("usersex", user.getSex());
            cv.put("userimg", user.getUserImg());
            cv.put("signature", user.getSignature());

            String[] args = {String.valueOf(user.getUserName())};
            int i = db.update("t_user", cv, "username=? ", args);

            if (i > 0) {
                isSuccess = true;
            } else {
                isSuccess = false;
            }

            db.setTransactionSuccessful();// 事务成功
        } finally {
            db.endTransaction();// 结束事务
        }
        return isSuccess;
    }

    public boolean isExiteByUserName(String userName) {
        boolean isExite = false;
        Cursor cur = null;
        db.beginTransaction();// 开始事务
        try {
            String sql = "SELECT * FROM t_user WHERE username = '" + userName + "';";
            cur = db.rawQuery(sql, null);
            if (cur.getCount() > 0) {
                isExite = true;
            } else {
                isExite = false;
            }

            db.setTransactionSuccessful();// 事务成功
        } finally {
            cur.close();
            db.endTransaction();// 结束事务
        }
        return isExite;
    }

    public boolean insertUserInfo(User user) {
        boolean isExite = false;
        long i = 0;
        db.beginTransaction();// 开始事务
        try {
            ContentValues values = new ContentValues();

            values.put("username", user.getUserName());
            values.put("password", user.getPassWord());
            values.put("userimg", user.getUserImg());

            i = db.insert("t_user", "userid", values);

            db.setTransactionSuccessful();// 事务成功
        } finally {
            db.endTransaction();// 结束事务
        }
        if (i != 0) {
            isExite = true;
        } else {
            isExite = false;
        }
        return isExite;
    }

    //图片转换成可存储的类型
    private byte[] getPicture(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    //---------------------------------t_apptime-----------------------------
    public boolean isExiteByappName(String appPackageName) {
        boolean isExite = false;
        Cursor cur = null;
        db.beginTransaction();// 开始事务
        try {
            //因为汉字的问题，导致了数据库内查询失败，目前想到一种解决办法，在拿app名字的时候同时拿到app的包名
            //修改库，修改实体类，使用包名来进行查询操作
            String sql = "SELECT appname FROM t_app WHERE apppackagename = '" + appPackageName + "';";
            cur = db.rawQuery(sql, null);
            if (cur.getCount() > 0) {
                isExite = true;
            } else {
                isExite = false;
            }

            db.setTransactionSuccessful();// 事务成功
        } finally {
            cur.close();
            db.endTransaction();// 结束事务
        }
        return isExite;
    }

    public void insertAppDurationTot_apptime(List<AppInfo> AppInfos, int year, int month, int day) {
        db.beginTransaction();// 开始事务
        try {
            for (int i = 0; i < AppInfos.size(); i++) {
                AppInfo info = AppInfos.get(i);
                ContentValues values = new ContentValues();

                values.put("userid", UserInfoUtil.getUserId());
                values.put("appname", info.getAppName());
                values.put("apppackagename", info.getAppPackageName());
                values.put("year", year);
                values.put("month", month);
                values.put("day", day);
                if (info.getAppDuration() != null && info.getAppDuration() != "") {
                    values.put("appduration", info.getAppDuration());
                }else {
                    values.put("appduration", 0);
                }

                db.insert("t_apptime", "logid", values);
            }
            db.setTransactionSuccessful();// 事务成功
        } finally {
            db.endTransaction();// 结束事务
        }
    }

    public void updateAppDurationTot_apptime(List<AppInfo> AppInfos, int year, int month, int day) {
        db.beginTransaction();// 开始事务
        try {
            for (AppInfo info : AppInfos) {
                ContentValues cv = new ContentValues();

                cv.put("appduration", info.getAppDuration());
                String[] args = {String.valueOf(info.getAppPackageName()), String.valueOf(year), String.valueOf(month), String.valueOf(day)};
                db.update("t_apptime", cv, "apppackagename=? and year=? and month=? and day=?", args);
            }
            db.setTransactionSuccessful();// 事务成功
        } finally {
            db.endTransaction();// 结束事务
        }
    }

    public ArrayList<AppInfo> findAppListByMonth(int userid, int year, int month, ArrayList<AppInfo> appInfos) {
        db.beginTransaction();// 开始事务
        Cursor cursor = null;
        ArrayList<AppInfo> appInfos1 = new ArrayList<AppInfo>();
        try {
            for (int i = 0; i < appInfos.size(); i++) {
                AppInfo appInfo = appInfos.get(i);
                String sql = "select * from t_apptime where userid = " + userid +
                        " and year = " + year + " and month = " + month +
                        " and apppackagename = " + "'" + appInfo.getAppPackageName() + "';";
                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    int count = 0;
                    while (cursor.moveToNext()) {
                        if (cursor.getString(cursor.getColumnIndex("appduration")) != null){
                            count = count + Integer.parseInt(cursor.getString(cursor.getColumnIndex("appduration")));
                        }
                        appInfo.setAppName(cursor.getString(cursor.getColumnIndex("appname")));
                        appInfo.setAppPackageName(cursor.getString(cursor.getColumnIndex("apppackagename")));
                        appInfo.setAppDuration(count+"");

                        LogUtil.e("DBManageAppInfo=====", "appName:" + appInfo.getAppName() + "  appDuration:" + appInfo.getAppDuration() + "  appIcon:" + appInfo.getAppIcon());

                    }
                    appInfos1.add(appInfo);
                    cursor.close();
                }
            }
            db.setTransactionSuccessful();// 事务成功
        } finally {
            db.endTransaction();// 结束事务
        }
        return appInfos1;
    }


    public ArrayList<AppInfo> findAppListByDay(int userid, int year, int month, int day, ArrayList<AppInfo> appInfos) {
        db.beginTransaction();// 开始事务
        Cursor cursor = null;
        ArrayList<AppInfo> appInfos1 = new ArrayList<AppInfo>();
        try {
            for (int i = 0; i < appInfos.size(); i++) {
                AppInfo appInfo = appInfos.get(i);
                String sql = "select * from t_apptime where userid = " + userid +
                        " and year = " + year + " and month = " + month +
                        " and day = " + day + " and apppackagename = " + "'" + appInfo.getAppPackageName() + "';";
                cursor = db.rawQuery(sql, null);
                if (cursor.getCount() > 0) {
//                    while (cursor.moveToNext()) {
                    cursor.moveToLast();
                    appInfo.setAppName(cursor.getString(cursor.getColumnIndex("appname")));
                    appInfo.setAppPackageName(cursor.getString(cursor.getColumnIndex("apppackagename")));
                    appInfo.setAppDuration(cursor.getString(cursor.getColumnIndex("appduration")));

                    LogUtil.e("DBManageAppInfo=====", "appName:" + appInfo.getAppName() + "  appDuration:" + appInfo.getAppDuration() + "  appIcon:" + appInfo.getAppIcon());

                    appInfos1.add(appInfo);
//                    }
                    cursor.close();
                }
            }
            db.setTransactionSuccessful();// 事务成功
        } finally {
            db.endTransaction();// 结束事务
        }
        return appInfos1;
    }

    //---------------------------------t_app-----------------------------
    public void insertAppContentsTot_app(List<AppInfo> AppInfos) {
        db.beginTransaction();// 开始事务
        try {
            for (AppInfo info : AppInfos) {
                //如果库中不存在该appPackageName 向t_app中插入数据
                if (!isExiteByappName(info.getAppPackageName())) {

                    ContentValues values = new ContentValues();

                    //将图片转为字节流
                    Bitmap bmp = (((BitmapDrawable) info.getAppIcon()).getBitmap());
                    final ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, os);

                    values.put("userid", UserInfoUtil.getUserId());
                    values.put("appname", info.getAppName());
                    values.put("appicon", os.toByteArray());
                    values.put("apppackagename", info.getAppPackageName());

                    long i = db.insert("t_app", "appid", values);
                    LogUtil.e("add操作：", i + "name:" + values.getAsString("appname") + "    Packagename:" + values.getAsString("apppackagename"));
                }

            }
            db.setTransactionSuccessful();// 事务成功
        } finally {
            db.endTransaction();// 结束事务
        }
    }

    public ArrayList<AppInfo> findAppListByUId(int userid) {
        db.beginTransaction();// 开始事务
        ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
        Cursor cursor = null;
        try {
            String sql = "select * from t_app where userid = '" + userid + "';";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                AppInfo appInfo = new AppInfo();
                appInfo.setAppName(cursor.getString(cursor.getColumnIndex("appname")));
                appInfo.setAppPackageName(cursor.getString(cursor.getColumnIndex("apppackagename")));

                byte[] image = cursor.getBlob(cursor.getColumnIndex("appicon"));
                ByteArrayInputStream bais = new ByteArrayInputStream(image);
                Drawable appicon = Drawable.createFromStream(bais, "imageflag");
                appInfo.setAppIcon(appicon);

                appInfos.add(appInfo);
            }
            db.setTransactionSuccessful();// 事务成功
        } finally {
            cursor.close();
            db.endTransaction();// 结束事务
        }
        return appInfos;
    }

    public void closeDB() {
        db.close();
    }
}
