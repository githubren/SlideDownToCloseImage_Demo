package com.example.yfsl.slidedowntocloseimage_demo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
//    private Button btn;
//    private ImageView picture;
    private RecyclerView recyclerView;
    private List<Bitmap> bitmaps;
    private List<Integer> imageIds;
    private int imageWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--------动态设置item大小和item之间间隙-----------
        //获取屏幕相对密度
        float scale = getResources().getDisplayMetrics().density;
        //dip2px 将两个item之间给定的间隙值（单位：dp）转换为屏幕的像素值（单位：px） 两个item之间间隙设置为5dp
        int space = (int) (5 * scale + 0.5f);
        //获取屏幕宽度值 计算出每个item的宽度   recyclerview采用网格布局 排3列 那么整个屏幕的分配为： 3个item的宽度 + 2个间隙的宽度
        imageWidth = (ScreenUtils.getScreenWidth(this) - 2 * space)/3;
        //-------------------------------------------------

//        picture = findViewById(R.id.picture);
        //获取recyclerview控件
        recyclerView = findViewById(R.id.recyclerview);
        //初始化两个集合
        if (bitmaps == null || imageIds == null) {
            bitmaps = new ArrayList<>();
            imageIds = new ArrayList<>();
        }
        //往集合中添加元素
        imageIds.add(R.drawable.icon_iv_loading);
        imageIds.add(R.drawable.icon_doc);
        imageIds.add(R.drawable.icon_update_rocket);
        //生成缩略图
        for (int i = 0;i<imageIds.size();i++){
//            Bitmap bitmap = getBitmap(imageIds.get(i),imageWidth);
            int imageId = imageIds.get(i);
            //调用方法getImageThumbnail（）生成缩略图 传入3个参数 1：图片放在drawable目录下时的图片id 2：缩放后图片的宽 3：缩放后图片的高
            Bitmap bitmap = getImageThumbnail(imageId,imageWidth,imageWidth);
            bitmaps.add(bitmap);
        }
        //recyclerview设置布局  网格布局 排3列
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        //创建recyclerview的adapter对象 传入两个集合
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,bitmaps,imageIds);
        //recyclerview设置adapter
        recyclerView.setAdapter(recyclerViewAdapter);
        //recyclerview中item的点击事件
        /**
         * 点击item 进行页面跳转操作  查看大图采用在新页面中用viewpager+fragment的方式
         * 因为是仿微信查看照片 其中有进入和退出的动画
         * 所以在页面跳转前需要记录此时item（缩略图）的位置信息
         * 创建一个图片信息实体类（ImageInfo）来存储图片位置信息 方便数据的传送
         */
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int id) {
                //创建Intent对象 进行页面跳转
                Intent intent = new Intent(MainActivity.this,ShowPictureActivity.class);
                //创建一个含有两个元素的数组 存放被点击view（图片）的坐标
                int[] imageItemLocation = new int[2];
                //获取view的坐标 传入数组
                view.getLocationOnScreen(imageItemLocation);
                //创建实体类对象
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setLEFT(imageItemLocation[0]);//设置X坐标
                imageInfo.setTOP(imageItemLocation[1]);//设置Y坐标
                imageInfo.setWIDTH(imageWidth);//设置宽度
                imageInfo.setHEIGHT(imageWidth);//设置高度
                imageInfo.setId(id);//设置当前被点击view的id
                imageInfo.setImageId(imageIds.get(id));//设置图片的drawableId

                intent.putExtra("imageInfo",imageInfo);
//                        .putExtra("top",imageItemLocation[1])
//                        .putExtra("width",imageWidth)
//                        .putExtra("height",imageWidth)
//                        .putExtra("id",id);
                startActivity(intent);
                //去掉自带的activity跳转动画
                overridePendingTransition(0,0);
            }
        });

//        picture.setImageBitmap(bitmap);
//
//        picture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,ShowPictureActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btn = findViewById(R.id.btn);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,ShowPictureActivity.class);
//                startActivity(intent);
//            }
//        });
//        //获取drawable目录下图片的String类型路径
//        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
//                + getResources().getResourcePackageName(R.drawable.icon_iv_loading) + "/"
//                + getResources().getResourceTypeName(R.drawable.icon_iv_loading) + "/"
//                + getResources().getResourceEntryName(R.drawable.icon_iv_loading);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
//        float realWidth = options.outWidth;
//        float realHeight = options.outHeight;
//        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth)/100);
//        options.inSampleSize = scale;
//        options.inJustDecodeBounds = false;
//        bitmap = BitmapFactory.decodeFile(path,options);
//
//        image.setImageBitmap(bitmap);
//        Glide
//                .with(this)
//                .load(bitmap)
//                .thumbnail(0.2f)
//                .into(image);
    }

    /**
     * 获取缩略图
     * @param id
     * @param width
     * @return
     */
    @SuppressLint("ResourceType")
    private Bitmap getBitmap(int id,int width) {
        InputStream is = getResources().openRawResource(id);
        BitmapDrawable image = new BitmapDrawable(is);
        Bitmap bitmap = image.getBitmap();
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,width,width);
        return bitmap;
    }

    /**
     * 获取缩略图
     * @param imageId
     * @param width
     * @param height
     * @return
     */
    private Bitmap getImageThumbnail(int imageId, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeResource(getResources(),imageId,options);
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        options.inJustDecodeBounds = false; // 设为 false
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeResource(getResources(),imageId,options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
