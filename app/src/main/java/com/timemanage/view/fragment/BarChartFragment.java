package com.timemanage.view.fragment;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.timemanage.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * 条形图展示页面
 * Created by Yawen_Li on 2017/1/17.
 */
public class BarChartFragment extends BaseFragment {

    private View mView;

    private Random random;

    private BarChart chart;
    private BarData data;
    private BarDataSet dataSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_barchart, container, false);

            chart = new BarChart(getContext());

            /**图表具体设置*/
            ArrayList<BarEntry> entries = new ArrayList<>();//显示条目
            ArrayList<String> xVals = new ArrayList<String>();//横坐标标签
            random=new Random();//随机数
            for(int i=0;i<12;i++){
                float profit= random.nextFloat()*1000;
                //entries.add(BarEntry(float val,int positon);
                entries.add(new BarEntry(profit,i));
                xVals.add((i+1)+"月");
            }
            dataSet = new BarDataSet(entries, "公司年利润报表");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            data = new BarData(xVals, dataSet);
            chart.setData(data);
            //设置可滑动展示
            Matrix m = new Matrix();
            m.postScale(1.5f,1f);//两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的1.5倍
            chart.getViewPortHandler().refresh(m,chart,false);//将图表动画显示之前进行缩放

            //设置Y方向上动画animateY(int time);
            chart.animateY(3000);
            //图表描述
            chart.setDescription("公司前半年财务报表(单位：万元)");

            mView = chart;
        }
        return mView;
    }
}
