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
import android.provider.MediaStore;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    private Button btn;
//    private ImageView picture;
    private RecyclerView recyclerView;
    private List<Bitmap> bitmaps;
    private int imageWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        float scale = getResources().getDisplayMetrics().density;
        int space = (int) (5 * scale + 0.5f);
        imageWidth = (ScreenUtils.getScreenWidth(this) - 2 * space)/3;
        //生成缩略图

//        picture = findViewById(R.id.picture);
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new GridLayoutManager(this,3));


        @SuppressLint("ResourceType")
        InputStream is = getResources().openRawResource(R.drawable.icon_iv_loading);
        BitmapDrawable image = new BitmapDrawable(is);
        Bitmap bitmap = image.getBitmap();
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,400,400);
        picture.setImageBitmap(bitmap);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ShowPictureActivity.class);
                startActivity(intent);
            }
        });
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
//
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
}
