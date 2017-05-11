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

    public AppListAdapter(Context context, ArrayList<AppInfo> dates) {
        Log.e("appInfos", dates.size() + "");
        this.context = context;
        this.dates = dates;
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
            holder.iv_duration = (ImageView) convertView.findViewById(R.id.iv_duration);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_appname = (TextView) convertView.findViewById(R.id.tv_appname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppInfo appInfo = dates.get(position);


        holder.iv_duration.setMinimumWidth(80);
        holder.tv_time.setText(appInfo.getAppDuration());
        holder.iv_appicon.setImageDrawable(appInfo.getAppIcon());
        holder.tv_appname.setText(appInfo.getAppName());
        LogUtil.e("AppInfo=====","appName:"+appInfo.getAppName()+"  appDuration:"+appInfo.getAppDuration()+"  appIcon:"+appInfo.getAppIcon());

        return convertView;
    }

    public class ViewHolder {
        ImageView iv_appicon;
        ImageView iv_duration;
        TextView tv_time;
        TextView tv_appname;
    }
}
