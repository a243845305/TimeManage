package com.timemanage.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.timemanage.utils.ActivityCollectorUtil;

/**
 * Created by Yawen_Li on 2017/1/13.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollectorUtil.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1

    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollectorUtil.removeActivity(this);
    }

}
