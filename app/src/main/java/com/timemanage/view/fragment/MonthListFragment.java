package com.timemanage.view.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.timemanage.R;
import com.timemanage.adapter.AppListAdapter;
import com.timemanage.bean.AppInfo;
import com.timemanage.presenter.fragment_presenter.DateListPresenter;
import com.timemanage.presenter.fragment_presenter.MonthListPresenter;
import com.timemanage.presenter.fragment_presenter_interface.IDateListPresenter;
import com.timemanage.presenter.fragment_presenter_interface.IMonthListPresenter;
import com.timemanage.utils.ConstantUtil;
import com.timemanage.utils.LogUtil;
import com.timemanage.view.fragment_interface.IDateListFragment;
import com.timemanage.view.fragment_interface.IMonthListFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Yawen_Li on 2017/5/11.
 */
public class MonthListFragment extends BaseFragment implements IMonthListFragment, View.OnClickListener {
    private View mView;
    private ListView lv_applist;
    private TextView tv_date;
    private View emptyView;

    private IMonthListPresenter iMonthListPresenter;
    private static Myhandler myhandler;
    private Calendar c;
    int year,month,day;
    private Context context;
    private ArrayList<AppInfo> appInfos;
    private AppListAdapter appListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myhandler = new Myhandler(MonthListFragment.this);
        //接口绑定
        iMonthListPresenter = new MonthListPresenter(MonthListFragment.this, MonthListFragment.this.getContext(), myhandler);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_monthlist, container, false);
            emptyView = mView.findViewById(R.id.emptyView);
            tv_date = (TextView) mView.findViewById(R.id.tv_date);
            lv_applist = (ListView) mView.findViewById(R.id.lv_apptimelist);
            context = MonthListFragment.this.getActivity();

            appInfos = new ArrayList<>();
            c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            String date = year + "/" + (month + 1) ;
            tv_date.setText(date);
            tv_date.setOnClickListener(this);

            initView();
        }
        return mView;
    }


    private void initView() {
        iMonthListPresenter.quertInitDatas();
        LogUtil.d("ListAppTimeFragment", "1、初始化View，获取数据");
        if (appInfos.isEmpty()) {
            lv_applist.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
            lv_applist.setVisibility(View.VISIBLE);
            appListAdapter = new AppListAdapter(context, appInfos);
            LogUtil.d("ListAppTimeFragment", "2、有数据初始化View");
            lv_applist.setAdapter(appListAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
                showDatePickerDialog();
                break;
            default:
                break;
        }
    }

    //===================显示日期============================
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,AlertDialog.THEME_HOLO_LIGHT,Datelistener,year,month,day);
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener Datelistener=new DatePickerDialog.OnDateSetListener()
    {
        /**params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear,int dayOfMonth) {


            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year=myyear;
            month=monthOfYear;
            day = dayOfMonth;
            iMonthListPresenter.queryDatasByMonth(year, month);
//            updateView(appInfos);
            //更新日期
            updateDate();

        }
        //当DatePickerDialog关闭时，更新日期显示
        private void updateDate()
        {
            //在TextView上显示日期
            tv_date.setText(year+"/"+(month+1));
        }
    };

    public class Myhandler extends Handler {
        WeakReference<MonthListFragment> weakRefersence;

        public Myhandler(MonthListFragment fragment) {
            weakRefersence = new WeakReference<MonthListFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtil.GET_NET_DATA:
                    LogUtil.d("MonthListFragment", "5、收到通知，数据已经更新，拿数据，更新页面，执行updateView方法");
                    appInfos = iMonthListPresenter.getDatasByMonth();
                    updateView(appInfos);
                    break;
                case ConstantUtil.INIT_DATA:
                    LogUtil.d("MonthListFragment", "5、收到通知，数据已经更新，拿数据，更新页面，执行updateView方法");
                    appInfos = iMonthListPresenter.getDatas();
                    updateView(appInfos);
                    break;
                default:
                    break;
            }
        }
    }

    //================================接口方法============================
    @Override
    public void updateView(ArrayList<AppInfo> appInfos) {

        if (appInfos.isEmpty()) {
            lv_applist.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            LogUtil.d("MonthListFragment", "7、仍然是空数据");
        } else {
            LogUtil.d("MonthListFragment", "8、成功拿到数据，更新页面");
            emptyView.setVisibility(View.GONE);
            lv_applist.setVisibility(View.VISIBLE);
            AppInfo appInfo = appInfos.get(1);
            LogUtil.e("List_AppInfo=====", "appName:" + appInfo.getAppName() + "  appDuration:" + appInfo.getAppDuration() + "  appIcon:" + appInfo.getAppIcon());

//            appInfos = iMonthListPresenter.getDatas();

            appListAdapter = new AppListAdapter(context, appInfos);
            lv_applist.setAdapter(appListAdapter);
        }
    }
}
