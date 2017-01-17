package com.timemanage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yawen_Li on 2017/1/17.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fmList = new ArrayList<Fragment>();

    public FragmentAdapter(FragmentManager fm ,List<Fragment> fmList) {
        super(fm);
        this.fmList = fmList;
    }

    @Override
    public Fragment getItem(int position) {
        return fmList.get(position);
    }

    @Override
    public int getCount() {
        if (fmList == null)
            return 0;
        else
            return fmList.size();
    }
}
