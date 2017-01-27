package com.timemanage.view.activity;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;

import com.timemanage.R;
import com.timemanage.view.fragment.DateChartFragment;
import com.timemanage.view.fragment.MonthChartFragment;
import com.timemanage.view.fragment.WeekChartFragment;

/**
 * @author Yawen_Li
 * @date 2017/01/25
 *
 * 对数据进行统计展示的页面
 */

public class StatisticsActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener ,View.OnClickListener{

    private Toolbar toolbar;

    private RadioGroup radioGroup;

    private Fragment[] mFragments;
    private int mIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_statistics);


        initView();
        initFragment();
    }

    private void initView(){


        radioGroup = (RadioGroup) findViewById(R.id.rd_group);
        toolbar = (Toolbar) findViewById(R.id.stat_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.rdbtn_date);
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,StatisticsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.rdbtn_date:
                setIndexSelected(0);
                break;
            case R.id.rdbtn_week:
                setIndexSelected(1);
                break;
            case R.id.rdbtn_month:
                setIndexSelected(2);
                break;
            default:
                break;
        }
    }

    private void initFragment() {
        DateChartFragment dateChartFragment = new DateChartFragment();
        WeekChartFragment weekChartFragment = new WeekChartFragment();
        MonthChartFragment monthChartFragment = new MonthChartFragment();


        //添加到数组
        mFragments = new Fragment[]{dateChartFragment,weekChartFragment,monthChartFragment};

        //开启事务

        FragmentTransaction ft =
                getSupportFragmentManager().beginTransaction();

        //添加首页
        ft.add(R.id.fl_content,dateChartFragment).commit();

        //默认设置为第0个
        setIndexSelected(0);


    }

    private void setIndexSelected(int index) {

        if(mIndex==index){
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();


        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if(!mFragments[index].isAdded()){
            ft.add(R.id.fl_content,mFragments[index]).show(mFragments[index]);
        }else {
            ft.show(mFragments[index]);
        }

        ft.commit();
        //再次赋值
        mIndex=index;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
    }

}
