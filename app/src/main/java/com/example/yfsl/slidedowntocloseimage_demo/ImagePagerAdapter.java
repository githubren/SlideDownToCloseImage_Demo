package com.example.yfsl.slidedowntocloseimage_demo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.jchou.imagereview.ui.ImageDetailFragment;

import java.util.ArrayList;
import java.util.List;

class ImagePagerAdapter extends FragmentStatePagerAdapter {
    private DragViewPager mPager;
    private ArrayList<Fragment> fragments;

    public ImagePagerAdapter(FragmentManager fm, DragViewPager mPager, List<String> datas) {
        super(fm);
        this.mPager = mPager;
        mPager.setAdapter(this);
        updataDatas(datas);
    }

    private void updataDatas(List<String> datas) {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0;i<datas.size();i++){
            final ImageDetailFragment imageDetailFragment = ImageDetailFragment.newInstance(datas.get(i));
            imageDetailFragment.setOnImageListener(new ImageDetailFragment.OnImageListener() {
                @Override
                public void onInit() {
                    View view = imageDetailFragment.getView();
                    mPager.setCurrentShowView(view);
                }
            });
            fragmentList.add(imageDetailFragment);
        }
        setViewList(fragmentList);
    }

    private void setViewList(ArrayList<Fragment> fragmentList) {
        if (fragments != null){
            fragments.clear();
        }
        this.fragments = fragmentList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
}
