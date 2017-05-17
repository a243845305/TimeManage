package com.timemanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timemanage.R;
import com.timemanage.bean.Developer;
import com.timemanage.utils.ConstantUtil;

import java.util.ArrayList;

/**
 * Created by Yawen_Li on 2017/5/17.
 */
public class TeamListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Developer> dates;

    public TeamListAdapter(Context context, ArrayList<Developer> dates){
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
            convertView = inflater.inflate(R.layout.item_teamlist, null);
            holder = new ViewHolder();

            holder.iv_teamimg = (ImageView) convertView.findViewById(R.id.iv_depimg);
            holder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
            holder.tv_depname = (TextView) convertView.findViewById(R.id.tv_depname);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Developer dep = dates.get(position);

        holder.tv_position.setText(dep.getdepPosition());
        holder.tv_depname.setText(dep.getdepName());
        Picasso.with(context).load(ConstantUtil.BASE_URL+dep.getdepImg()).fit().into(holder.iv_teamimg);
        return convertView;
    }

    public class ViewHolder {
        ImageView iv_teamimg;
        TextView tv_position;
        TextView tv_depname;
    }
}
