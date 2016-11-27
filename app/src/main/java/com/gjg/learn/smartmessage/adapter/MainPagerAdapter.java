package com.gjg.learn.smartmessage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Junguang_Gao on 2016/11/14.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public MainPagerAdapter(FragmentManager fragmentManager,List<Fragment> fragments){
        super(fragmentManager);
        this.fragments=fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
