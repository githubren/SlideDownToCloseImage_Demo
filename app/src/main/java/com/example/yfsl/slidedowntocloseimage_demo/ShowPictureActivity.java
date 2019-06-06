package com.example.yfsl.slidedowntocloseimage_demo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 显示照片的activity
 * 采用viewpager+fragment的方式
 * 一个fragment对应一张图片
 */

public class ShowPictureActivity extends AppCompatActivity {
//    private ImageView picture;
    private ViewPager viewPager;
//    private FriendCircleView viewPager;
    private Map<Integer,Fragment> fragments;
    private List<Integer> imageIds;
    private int id;
    private ImageInfo imageInfo;

    public void getArgus(){
        Intent intent = getIntent();
        if (intent != null){
//            id = intent.getIntExtra("id",0);
//            left = intent.getIntExtra("left",0);
//            top = intent.getIntExtra("top",0);
//            width = intent.getIntExtra("width",0);
//            height = intent.getIntExtra("height",0);
            imageInfo = intent.getParcelableExtra("imageInfo");
//            imageIds = intent.getIntegerArrayListExtra("id_list");
        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpicture);

        getArgus();
        //获取点击的view的id
        id = imageInfo.getId();

//        picture = findViewById(R.id.picture_show);
        viewPager = findViewById(R.id.viewPager);

        fragments = new HashMap<>();
        imageIds = new ArrayList<>();

        imageIds.add(R.drawable.icon_iv_loading);
        imageIds.add(R.drawable.icon_doc);
        imageIds.add(R.drawable.icon_update_rocket);
        if (imageIds == null || imageIds.isEmpty()) return;
        for (int i = 0;i<imageIds.size();i++){
            fragments.put(i,ImageFragment.getInstance(imageInfo));
        }

        //viewpager设置adapter
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(),fragments));
        Log.e("TAG","fragments.size = "+fragments.size());

        /**
         * 实现点击哪张图片就显示那张  而不是总从第一张开始显示
         * 在MainActivity中点击图片的时候 将其viewID传递过来
         * 在此处对fragments集合做遍历 将两者id相同的那个fragment显示在页面上
         */
        for (int i = 0;i<fragments.size();i++){
            if (id == i){
                viewPager.setCurrentItem(i);
            }
        }
        //viewpager设置滑动监听
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
