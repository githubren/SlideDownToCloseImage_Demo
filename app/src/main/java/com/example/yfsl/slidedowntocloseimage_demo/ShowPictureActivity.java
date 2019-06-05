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

public class ShowPictureActivity extends AppCompatActivity {
//    private ImageView picture;
    private ViewPager viewPager;
//    private FriendCircleView viewPager;
    private Map<Integer,Fragment> fragments;
    private List<Integer> imageIds;
    private int id;

    public int getArgus(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
//            imageIds = intent.getIntegerArrayListExtra("id_list");
        }
        return id;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpicture);

        getArgus();

//        picture = findViewById(R.id.picture_show);
        viewPager = findViewById(R.id.viewPager);

        fragments = new HashMap<>();
        imageIds = new ArrayList<>();

        imageIds.add(R.drawable.icon_iv_loading);
        imageIds.add(R.drawable.icon_doc);
        imageIds.add(R.drawable.icon_update_rocket);
        if (imageIds == null || imageIds.isEmpty()) return;
        for (int i = 0;i<imageIds.size();i++){
            fragments.put(i,ImageFragment.getInstance(imageIds.get(i)));
        }


        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(),fragments));
        Log.e("TAG","fragments.size = "+fragments.size());
        //实现点击哪张图片就显示那张  而不是总从第一张开始显示
        for (int i = 0;i<fragments.size();i++){
            if (id == i){
                viewPager.setCurrentItem(i);
            }
        }

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
