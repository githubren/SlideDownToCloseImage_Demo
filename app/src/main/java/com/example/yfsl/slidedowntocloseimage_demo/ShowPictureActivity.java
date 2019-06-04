package com.example.yfsl.slidedowntocloseimage_demo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ShowPictureActivity extends AppCompatActivity {
//    private ImageView picture;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private List<Integer> imageIds;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpicture);

//        picture = findViewById(R.id.picture_show);
        viewPager = findViewById(R.id.viewPager);

        fragments = new ArrayList<>();
        imageIds = new ArrayList<>();

        imageIds.add(R.drawable.icon_iv_loading);
        imageIds.add(R.drawable.icon_doc);
        imageIds.add(R.drawable.icon_update_rocket);

        for (int i = 0;i<imageIds.size();i++){
            fragments.add(ImageFragment.getInstance(imageIds.get(i)));
        }

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(),fragments));
        Log.e("TAG","fragments.size = "+fragments.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                viewPager.setCurrentItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
