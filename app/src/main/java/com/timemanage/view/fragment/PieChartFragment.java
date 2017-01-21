package com.timemanage.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.timemanage.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.zip.Inflater;

/**
 *
 * 环形展示页面
 * Created by Yawen_Li on 2017/1/17.
 */
public class PieChartFragment extends BaseFragment {

    private View mView;

    private PieChart pieChart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_piechart,container,false);
            pieChart = (PieChart) mView.findViewById(R.id.piechart);

        }
        return mView;
    }
}
