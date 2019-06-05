package com.example.yfsl.slidedowntocloseimage_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImageFragment extends Fragment {
    private FriendCircleView_1 picture;
    private static final String IMAGE_INFO = "image_info";
    private int imageId;
    private int left,top,width,height;
    private ImageInfo imageInfo;

    public static ImageFragment getInstance(ImageInfo imageInfo){
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IMAGE_INFO,imageInfo);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    public void getArgus(){
        Bundle bundle = getArguments();
        if (bundle != null){
            imageInfo = bundle.getParcelable(IMAGE_INFO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getArgus();
        left = imageInfo.getLEFT();
        top = imageInfo.getTOP();
        width = imageInfo.getWIDTH();
        height = imageInfo.getHEIGHT();
        imageId = imageInfo.getId();
        
        View view = inflater.inflate(R.layout.fragment_showpicture,container,false);
        picture = view.findViewById(R.id.picture);
        picture.setImageResource(imageId);
        picture.setViewCall(new FriendCircleView_1.FriendCircleViewCall() {
            @Override
            public void viewDestry() {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                //取消activity跳转动画
                getActivity().overridePendingTransition(0,0);
            }
        });
        return view;
    }
}
