package com.timemanage.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.timemanage.R;
import com.timemanage.adapter.AppListAdapter;
import com.timemanage.bean.AppInfo;
import com.timemanage.presenter.fragment_presenter.ListAppTimePresenter;
import com.timemanage.presenter.fragment_presenter_interface.IListAppTimePresenter;
import com.timemanage.utils.ConstantUtil;
import com.timemanage.utils.LogUtil;
import com.timemanage.view.fragment_interface.IListAppTimeFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Yawen_Li on 2017/5/11.
 */
public class ListAppTimeFragment extends BaseFragment implements IListAppTimeFragment {
    private View mView;
    private ListView lv_applist;
    private TextView tv_date;
    private View emptyView ;

    private IListAppTimePresenter iListAppTimePresenter;
    private static Myhandler myhandler;
    private Calendar c;
    private Context context;
    private ArrayList<AppInfo> appInfos;
    private AppListAdapter appListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myhandler = new Myhandler(ListAppTimeFragment.this);
        //接口绑定
        iListAppTimePresenter = new ListAppTimePresenter(ListAppTimeFragment.this,ListAppTimeFragment.this.getContext(),myhandler);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_listapptime, container, false);
            emptyView = mView.findViewById(R.id.emptyView);
            tv_date = (TextView) mView.findViewById(R.id.tv_date);
            lv_applist = (ListView) mView.findViewById(R.id.lv_apptimelist);
            context = ListAppTimeFragment.this.getActivity();

            appInfos = new ArrayList<>();
            c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            String date = year+"/"+(month+1)+"/"+day;
            tv_date.setText(date);

            initView();
        }
        return mView;
    }


    private void initView(){
        appInfos= iListAppTimePresenter.getDatas();
        LogUtil.d("ListAppTimeFragment", "1、初始化View，获取数据");
        if (appInfos.isEmpty()){
            lv_applist.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }else {
            emptyView.setVisibility(View.GONE);
            lv_applist.setVisibility(View.VISIBLE);
            appListAdapter =new AppListAdapter(context,appInfos);
            LogUtil.d("ListAppTimeFragment", "2、有数据初始化View");
            lv_applist.setAdapter(appListAdapter);
        }
    }

    public  class Myhandler extends Handler {
        WeakReference<ListAppTimeFragment> weakRefersence;
        public Myhandler(ListAppTimeFragment fragment) {
            weakRefersence =new  WeakReference<ListAppTimeFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantUtil.GET_NET_DATA:
                    LogUtil.d("PersonalCollectTradeF", "5、收到通知，数据已经更新，拿数据，更新页面，执行updateView方法");
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
        if (appInfos.isEmpty()){
            lv_applist.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            LogUtil.d("PersonalCollectTradeF", "7、仍然是空数据");
        }else {
            LogUtil.d("PersonalCollectTradeF", "8、成功拿到数据，更新页面");
            emptyView.setVisibility(View.GONE);
            lv_applist.setVisibility(View.VISIBLE);
            AppInfo appInfo = appInfos.get(1);
            LogUtil.e("List_AppInfo=====", "appName:" + appInfo.getAppName() + "  appDuration:" + appInfo.getAppDuration() + "  appIcon:" + appInfo.getAppIcon());

            appInfos =  iListAppTimePresenter.getDatas();

            appListAdapter = new AppListAdapter(context, appInfos);
            lv_applist.setAdapter(appListAdapter);
        }
    }
}
