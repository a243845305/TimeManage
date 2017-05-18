package com.timemanage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.timemanage.R;
import com.timemanage.bean.AppInfo;
import com.timemanage.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by Yawen_Li on 2017/5/11.
 */
public class AppListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AppInfo> dates;
    private double num;

    public AppListAdapter(Context context, ArrayList<AppInfo> dates) {
        Log.e("appInfos", dates.size() + "");
        this.context = context;
        this.dates = dates;
        num = 0.0;
        for (int i = 0; i < dates.size(); i++) {
            num = num + Integer.parseInt(dates.get(i).getAppDuration());
            LogUtil.e("Adapter","num:"+num+"   appduiation:"+dates.get(i).getAppDuration()+"  appName:"+dates.get(i).getAppName());
        }
        num = num + 0.1;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_applist, null);
            holder = new ViewHolder();
            holder.iv_appicon = (ImageView) convertView.findViewById(R.id.iv_appicon);
            holder.pb_duration = (ProgressBar) convertView.findViewById(R.id.pb_progressbar);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_appname = (TextView) convertView.findViewById(R.id.tv_appname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppInfo appInfo = dates.get(position);


        holder.pb_duration.setProgress((int)((Integer.parseInt(appInfo.getAppDuration()) / num) * 100));
        holder.tv_time.setText(appInfo.getAppDuration());
        holder.iv_appicon.setImageDrawable(appInfo.getAppIcon());
        holder.tv_appname.setText(appInfo.getAppName());
        LogUtil.e("AppInfo=====", "appName:" + appInfo.getAppName() + "  appDuration:" + appInfo.getAppDuration() + "  appIcon:" + appInfo.getAppIcon());

        return convertView;
    }

    public class ViewHolder {
        ImageView iv_appicon;
        ProgressBar pb_duration;
        TextView tv_time;
        TextView tv_appname;
    }
}
