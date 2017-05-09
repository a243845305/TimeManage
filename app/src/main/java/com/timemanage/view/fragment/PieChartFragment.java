package com.timemanage.view.fragment;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.timemanage.R;
import com.timemanage.db.DataBaseManager;
import com.timemanage.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * 环形展示页面
 * Created by Yawen_Li on 2017/1/17.
 */
public class PieChartFragment extends BaseFragment {

    private View mView;

    private PieChart pieChart;
    private PieData pieData;
    private PieDataSet pieDataSet;

    private String[] x = new String[] { "A类事物", "B类事物", "C类事物","D类事物" };

    // 凑成100 % 100
    private float[] y = { 10f, 60f, 10f, 20f };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_piechart,container,false);
            pieChart = (PieChart) mView.findViewById(R.id.fag_piechart);
            initView();
        }
        return mView;
    }

    private void initView(){
        // 设置 pieChart 图表基本属性
        pieChart.setUsePercentValues(false);            //使用百分比显示  
        pieChart.setDescription("饼状图设置");           //设置pieChart图表的描述
        pieChart.setDescriptionTextSize(14f);
        pieChart.setBackgroundColor(Color.YELLOW);      //设置pieChart图表背景色  
        pieChart.setExtraOffsets(5, 10, 60, 10);        //设置pieChart图表上下左右的偏移，类似于外边距  
        pieChart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]  
        pieChart.setRotationAngle(0);                   //设置pieChart图表起始角度  
        pieChart.setRotationEnabled(false);              //设置pieChart图表是否可以手动旋转
        pieChart.setHighlightPerTapEnabled(true);       //设置piecahrt图表点击Item高亮是否可用  
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);// 设置pieChart图表展示动画效果
        pieChart.setDrawSliceText(false);

        // 设置 pieChart 图表Item文本属性
//        pieChart.setDrawEntryLabels(true);              //设置pieChart是否只显示饼图上百分比不显示文字（true：下面属性才有效果）
//        pieChart.setEntryLabelColor(Color.WHITE);       //设置pieChart图表文本字体颜色
//        pieChart.setEntryLabelTypeface(mTfRegular);     //设置pieChart图表文本字体样式
//        pieChart.setEntryLabelTextSize(10f);            //设置pieChart图表文本字体大小

        // 设置 pieChart 内部圆环属性
        pieChart.setDrawHoleEnabled(true);              //是否显示PieChart内部圆环(true:下面属性才有意义)  
        pieChart.setHoleRadius(50f);                    //设置PieChart内部圆的半径(这里设置28.0f)
        pieChart.setTransparentCircleRadius(55f);       //设置PieChart内部透明圆的半径(这里设置31.0f)
        pieChart.setTransparentCircleColor(Color.BLACK);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色  
        pieChart.setTransparentCircleAlpha(50);         //设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数值越小越透明  
        pieChart.setHoleColor(Color.WHITE);             //设置PieChart内部圆的颜色  
        pieChart.setDrawCenterText(true);               //是否绘制PieChart内部中心文本（true：下面属性才有意义）  
//        pieChart.setCenterTextTypeface(mTfLight);       //设置PieChart内部圆文字的字体样式
        pieChart.setCenterText("Test");                 //设置PieChart内部圆文字的内容  
        pieChart.setCenterTextSize(10f);                //设置PieChart内部圆文字的大小
        pieChart.setCenterTextColor(Color.RED);         //设置PieChart内部圆文字的颜色  

        // pieChart添加数据
        setData();

        // 获取pieCahrt图列
        Legend l = pieChart.getLegend();
        l.setEnabled(true);                    //是否启用图列（true：下面属性才有意义）
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setForm(Legend.LegendForm.DEFAULT); //设置图例的形状
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(10);                    //设置图例的大小
        l.setFormToTextSpace(5f);            //设置每个图例实体中标签和形状之间的间距
        l.setWordWrapEnabled(true);           //设置图列换行(注意使用影响性能,仅适用legend位于图表下面)
        l.setXEntrySpace(10f);                //设置图例实体之间延X轴的间距（setOrientation = HORIZONTAL有效）
        l.setYEntrySpace(5f);                 //设置图例实体之间延Y轴的间距（setOrientation = VERTICAL 有效）
        l.setYOffset(0f);                     //设置比例块Y轴偏移量
        l.setTextSize(16f);                   //设置图例标签文本的大小
        l.setTextColor(Color.BLACK);          //设置图例标签文本的颜色

        
        pieChart.invalidate();
    }

    public void setData(){
        //准备x轴的数据，在i的位置上显示x[i]字符串
        ArrayList<String> xVals = new ArrayList<String>();
        //Entry包含两个重要的数据内容：position和该position的数值
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int xi=0;xi<x.length;xi++){
            xVals.add(xi,x[xi]);
            yVals.add(new Entry(y[xi],xi));
        }

        pieDataSet = new PieDataSet(yVals,"");
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.GRAY);
        pieDataSet.setColors(colors);

        pieData = new PieData(xVals,pieDataSet);

        // 设置成PercentFormatter将追加%号
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(16f);
        pieData.setValueTextColor(Color.BLACK);

        pieChart.setData(pieData);
    }
}
