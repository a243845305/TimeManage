package com.timemanage.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timemanage.R;

/**
 *
 * 统计周视图
 * Created by Yawen_Li on 2017/1/27.
 */
public class WeekChartFragment extends BaseFragment{

    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {

            mView = inflater.inflate(R.layout.fragment_week,container,false);
        }

        return mView;
    }
}
