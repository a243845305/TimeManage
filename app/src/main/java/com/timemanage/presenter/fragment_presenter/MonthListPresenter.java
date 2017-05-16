package com.timemanage.presenter.fragment_presenter;

import android.content.Context;
import android.os.Handler;

import com.timemanage.TimeManageAppliaction;
import com.timemanage.bean.AppInfo;
import com.timemanage.db.DataBaseManager;
import com.timemanage.presenter.fragment_presenter_interface.IDateListPresenter;
import com.timemanage.presenter.fragment_presenter_interface.IMonthListPresenter;
import com.timemanage.utils.UserInfoUtil;
import com.timemanage.view.fragment.DateListFragment;
import com.timemanage.view.fragment.MonthListFragment;
import com.timemanage.view.fragment_interface.IDateListFragment;
import com.timemanage.view.fragment_interface.IMonthListFragment;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Yawen_Li on 2017/5/11.
 */
public class MonthListPresenter implements IMonthListPresenter {

    private IMonthListFragment iMonthListFragment;
    private ArrayList<AppInfo> appInfos;
    private DataBaseManager dbManager;
    private Context context;
    private Handler handler;
    private Calendar c;

    public MonthListPresenter(IMonthListFragment iMonthListFragment, Context context, MonthListFragment.Myhandler myhandler){
        this.iMonthListFragment = iMonthListFragment;
        this.context = context;
        this.handler = myhandler;
        dbManager = new DataBaseManager(TimeManageAppliaction.getContext());
        c = Calendar.getInstance();
    }
    //===================接口方法===========================
    @Override
    public ArrayList<AppInfo> getDatas() {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        appInfos = new ArrayList<AppInfo>();

        appInfos = dbManager.findAppListByUId(Integer.parseInt(UserInfoUtil.getUserId()));
        appInfos = dbManager.findAppListByMonth(Integer.parseInt(UserInfoUtil.getUserId()),year,month,appInfos);

        if (appInfos.size()>0 && appInfos.get(0).getAppDuration() != null){
            return appInfos;
        }else {
            appInfos.clear();
            return appInfos;
        }


    }

    @Override
    public ArrayList<AppInfo> getDatasByMonth(int year, int month) {
        appInfos = new ArrayList<AppInfo>();

        appInfos = dbManager.findAppListByUId(Integer.parseInt(UserInfoUtil.getUserId()));
        appInfos = dbManager.findAppListByMonth(Integer.parseInt(UserInfoUtil.getUserId()),year,month+1,appInfos);

        if (appInfos.size()>0 && appInfos.get(0).getAppDuration() != null){
            return appInfos;
        }else {
            appInfos.clear();
            return appInfos;
        }
    }


}
