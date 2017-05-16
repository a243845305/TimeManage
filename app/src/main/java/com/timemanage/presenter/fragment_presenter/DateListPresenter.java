package com.timemanage.presenter.fragment_presenter;

import android.content.Context;
import android.os.Handler;

import com.timemanage.TimeManageAppliaction;
import com.timemanage.bean.AppInfo;
import com.timemanage.db.DataBaseManager;
import com.timemanage.presenter.fragment_presenter_interface.IDateListPresenter;
import com.timemanage.presenter.fragment_presenter_interface.IListAppTimePresenter;
import com.timemanage.utils.UserInfoUtil;
import com.timemanage.view.fragment.DateListFragment;
import com.timemanage.view.fragment.ListAppTimeFragment;
import com.timemanage.view.fragment_interface.IDateListFragment;
import com.timemanage.view.fragment_interface.IListAppTimeFragment;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Yawen_Li on 2017/5/11.
 */
public class DateListPresenter implements IDateListPresenter {

    private IDateListFragment iDateListFragment;
    private ArrayList<AppInfo> appInfos;
    private DataBaseManager dbManager;
    private Context context;
    private Handler handler;
    private Calendar c;

    public DateListPresenter(IDateListFragment iDateListFragment, Context context, DateListFragment.Myhandler myhandler){
        this.iDateListFragment = iDateListFragment;
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
        int day = c.get(Calendar.DAY_OF_MONTH);
        appInfos = new ArrayList<AppInfo>();

        appInfos = dbManager.findAppListByUId(Integer.parseInt(UserInfoUtil.getUserId()));
        appInfos = dbManager.findAppListByDay(Integer.parseInt(UserInfoUtil.getUserId()),year,month,day,appInfos);

        if (appInfos.size()>0 && appInfos.get(0).getAppDuration() != null){
            return appInfos;
        }else {
            appInfos.clear();
            return appInfos;
        }


    }

    @Override
    public ArrayList<AppInfo> getDatasByDay(int year, int month, int day) {
        appInfos = new ArrayList<AppInfo>();

        appInfos = dbManager.findAppListByUId(Integer.parseInt(UserInfoUtil.getUserId()));
        appInfos = dbManager.findAppListByDay(Integer.parseInt(UserInfoUtil.getUserId()),year,month+1,day,appInfos);

        if (appInfos.size()>0 && appInfos.get(0).getAppDuration() != null){
            return appInfos;
        }else {
            appInfos.clear();
            return appInfos;
        }
    }


}
