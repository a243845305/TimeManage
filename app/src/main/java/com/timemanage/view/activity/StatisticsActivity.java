package com.timemanage.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.timemanage.R;

/**
 * @author Yawen_Li
 * @date 2017/01/25
 *
 * 对数据进行统计展示的页面
 */

public class StatisticsActivity extends BaseActivity {

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_statistics);

        toolbar = (Toolbar) findViewById(R.id.stat_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,StatisticsActivity.class);
        context.startActivity(intent);
    }
}
