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

        float scale = getResources().getDisplayMetrics().density;
        int space = (int) (5 * scale + 0.5f);
        imageWidth = (ScreenUtils.getScreenWidth(this) - 2 * space)/3;


//        picture = findViewById(R.id.picture);
        recyclerView = findViewById(R.id.recyclerview);

        if (bitmaps == null || imageIds == null) {
            bitmaps = new ArrayList<>();
            imageIds = new ArrayList<>();
        }
        imageIds.add(R.drawable.icon_iv_loading);
        imageIds.add(R.drawable.icon_doc);
        imageIds.add(R.drawable.icon_update_rocket);
        //生成缩略图
        for (int i = 0;i<imageIds.size();i++){
//            Bitmap bitmap = getBitmap(imageIds.get(i),imageWidth);
            int imageId = imageIds.get(i);
            Bitmap bitmap = getImageThumbnail(imageId,imageWidth,imageWidth);
            bitmaps.add(bitmap);
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,bitmaps,imageIds);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int id) {
                Intent intent = new Intent(MainActivity.this,ShowPictureActivity.class);
                int[] imageItemLocation = new int[2];
                view.getLocationOnScreen(imageItemLocation);

                ImageInfo imageInfo = (ImageInfo) ImageInfo.CREATOR;
                imageInfo.setLEFT(imageItemLocation[0]);
                imageInfo.setTOP(imageItemLocation[1]);
                imageInfo.setWIDTH(imageWidth);
                imageInfo.setHEIGHT(imageWidth);
                imageInfo.setId(id);

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

    @SuppressLint("ResourceType")
    private Bitmap getBitmap(int id,int width) {
        InputStream is = getResources().openRawResource(id);
        BitmapDrawable image = new BitmapDrawable(is);
        Bitmap bitmap = image.getBitmap();
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,width,width);
        return bitmap;
    }

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
