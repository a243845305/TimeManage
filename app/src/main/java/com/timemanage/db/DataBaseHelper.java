package com.timemanage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.timemanage.utils.LogUtil;

/**
 * 创建数据库，创建需要的表
 * Created by Yawen_Li on 2017/1/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private String TAG = "DataBase";
    private static final String DB_NAME = "TimeManageDB.db"; //数据库名称
    private static final int version = 1; //数据库版本
    private static Context context;


    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        this.context = context;
        LogUtil.d("DB_PATH:", context.getDatabasePath(DB_NAME).toString());
    }

    private volatile static DataBaseHelper uniqueInstance;
    public static DataBaseHelper getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (DataBaseHelper.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new DataBaseHelper(context);
                }
            }
        }
        return uniqueInstance;
    }

    public boolean deleteDBByName() {
        context.deleteDatabase(DB_NAME);
        LogUtil.d("DB", "had deleted database:" + DB_NAME);
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists t_user(userid integer primary key autoincrement ," +
                "username text not null , " +
                "password text not null ," +
                "userimg bolb ," +
                "usersex text ," +
                "signature text ," +
                "usernickname text);";
        String sql1 = "create table if not exists t_app(appid integer primary key autoincrement ," +
                "userid integer not null ," +
                "appname text not null ," +
                "apppackagename text not null ,"+
                "appicon bolb );";
        String sql2 = "create table if not exists t_apptime(logid integer primary key autoincrement ," +
                "userid integer not null ," +
                "appname text not null ," +
                "apppackagename text not null ," +
                "appduration integer ," +
                "year integer ," +
                "month integer ," +
                "day integer );";
        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
        LogUtil.d(TAG,"onCreate exec");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //数据库版本更新时执行的方法
    }
}
