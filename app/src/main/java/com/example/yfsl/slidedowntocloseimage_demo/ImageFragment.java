package com.example.yfsl.slidedowntocloseimage_demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends Fragment {
    private ImageView picture;
    private static final String IMAGE_ID = "image_id";
    private int imageId;

    public static ImageFragment getInstance(int id){
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IMAGE_ID,id);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    public int getArgus(){
        Bundle bundle = getArguments();
        if (bundle != null){
            imageId = bundle.getInt(IMAGE_ID);
        }
        return imageId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getArgus();
        View view = inflater.inflate(R.layout.fragment_showpicture,container,false);
        picture = view.findViewById(R.id.picture);
        picture.setImageResource(imageId);
        return view;
    }
}
