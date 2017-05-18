package com.timemanage.presenter.fragment_presenter;

import android.content.Context;
import android.os.Handler;

import com.timemanage.TimeManageAppliaction;
import com.timemanage.bean.AppInfo;
import com.timemanage.db.DataBaseManager;
import com.timemanage.presenter.fragment_presenter_interface.IDateListPresenter;
import com.timemanage.presenter.fragment_presenter_interface.IMonthListPresenter;
import com.timemanage.utils.ConstantUtil;
import com.timemanage.utils.LogUtil;
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
    private ArrayList<AppInfo> initAppInfos;
    private DataBaseManager dbManager;
    private Context context;
    private Handler handler;
    private Calendar c;

    public MonthListPresenter(IMonthListFragment iMonthListFragment, Context context, MonthListFragment.Myhandler myhandler) {
        this.iMonthListFragment = iMonthListFragment;
        this.context = context;
        this.handler = myhandler;
        appInfos = new ArrayList<AppInfo>();
        initAppInfos = new ArrayList<AppInfo>();
        dbManager = new DataBaseManager(TimeManageAppliaction.getContext());
        c = Calendar.getInstance();
    }

    //===================接口方法===========================

    @Override
    public void quertInitDatas() {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        initAppInfos = dbManager.findAppListByUId(Integer.parseInt(UserInfoUtil.getUserId()));
        LogUtil.e("MonthListInitApp1","appName:"+initAppInfos.get(0).getAppName()+"   appDuration:"+initAppInfos.get(0).getAppDuration());

        initAppInfos = dbManager.findAppListByMonth(Integer.parseInt(UserInfoUtil.getUserId()), year, month, initAppInfos);
        LogUtil.e("MonthListInitApp2","appName:"+initAppInfos.get(0).getAppName()+"   appDuration:"+initAppInfos.get(0).getAppDuration());

        handler.sendEmptyMessage(ConstantUtil.INIT_DATA);
    }

    @Override
    public ArrayList<AppInfo> getDatas() {

        if (initAppInfos.size() > 0 && initAppInfos.get(0).getAppDuration() != null) {
            return initAppInfos;
        } else {
            initAppInfos.clear();
            return initAppInfos;
        }
    }

    @Override
    public void queryDatasByMonth(int year, int month) {

        appInfos = dbManager.findAppListByUId(Integer.parseInt(UserInfoUtil.getUserId()));
        appInfos = dbManager.findAppListByMonth(Integer.parseInt(UserInfoUtil.getUserId()), year, month + 1, appInfos);
//        LogUtil.e("MonthList","appName:"+appInfos.get(0).getAppName()+"   appDuration:"+appInfos.get(0).getAppDuration());
        handler.sendEmptyMessage(ConstantUtil.GET_NET_DATA);
    }

    @Override
    public ArrayList<AppInfo> getDatasByMonth(){

        if (appInfos.size() > 0 && appInfos.get(0).getAppDuration() != null) {
            return appInfos;
        } else {
            appInfos.clear();
            return appInfos;
        }
    }


}
