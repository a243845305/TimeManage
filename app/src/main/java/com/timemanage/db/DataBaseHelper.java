package com.timemanage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库，创建需要的表
 * Created by Yawen_Li on 2017/1/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TimeManageDB.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table t_user(userid integer primary key autoincrement ," +
                "username text not null , " +
                "password text not null ," +
                "userimg text);";
        String sql1 = "create table t_app(appid integer primary key autonicrement ," +
                "userid integer not null ," +
                "appname text not null ," +
                "appicon text not null );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
