package com.timemanage.presenter.fragment_presenter_interface;

import com.timemanage.bean.AppInfo;

import java.util.ArrayList;

/**
 * Created by Yawen_Li on 2017/5/11.
 */
public interface IMonthListPresenter {
    ArrayList<AppInfo> getDatas();
    ArrayList<AppInfo> getDatasByMonth(int year, int month);
}
