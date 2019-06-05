package com.example.yfsl.slidedowntocloseimage_demo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;
import java.util.Map;

public class MyAdapter extends FragmentStatePagerAdapter {
    private Map<Integer,Fragment> fragmentList;

    public MyAdapter(FragmentManager fm , Map<Integer,Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }


    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }


}
