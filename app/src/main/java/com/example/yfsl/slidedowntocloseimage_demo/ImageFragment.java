package com.example.yfsl.slidedowntocloseimage_demo;


import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * 显示照片的fragment
 * 使用自定义控件实现点击或下滑退出查看的功能（仿微信照片查看的效果）
 */

public class ImageFragment extends Fragment {
    private FriendCircleView picture;//自定义显示照片的控件
    private RelativeLayout fragment_bg;
    private static final String IMAGE_INFO = "image_info";
    private int imageId;
    private int left,top,width,height;
    private int leftDelta,topDelta;
    private float widthScale,heightScale;
    private ImageInfo imageInfo;

    public static ImageFragment getInstance(ImageInfo imageInfo){
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IMAGE_INFO,imageInfo);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    /**
     * 获取传递过来的ImageInfo对象
     */
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
        //获取包含的数据
        left = imageInfo.getLEFT();
        top = imageInfo.getTOP();
        width = imageInfo.getWIDTH();
        height = imageInfo.getHEIGHT();
        imageId = imageInfo.getImageId();
        //加载布局
        View view = inflater.inflate(R.layout.fragment_showpicture,container,false);
        picture = view.findViewById(R.id.picture);
        //放图片
        picture.setImageResource(imageId);
        fragment_bg = view.findViewById(R.id.fragment_rl);
        fragment_bg.setBackgroundColor(Color.BLACK);
        //获取控件宽高
        ViewTreeObserver observer = picture.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                picture.getViewTreeObserver().removeOnPreDrawListener(this);
                //创建数组存放大图的坐标数据
                int[] screenLocation = new int[2];
                //获取大图位置坐标 传入数组
                picture.getLocationOnScreen(screenLocation);
                //动画过程中转换的两张图之间横坐标偏移的值
                leftDelta = left - screenLocation[0];
                //动画过程中转换的两张图之间纵坐标偏移的值
                topDelta = top - screenLocation[1];
                //宽度变化的比例
                widthScale = width/picture.getWidth();
                //高度变化的比例
                heightScale = height/picture.getHeight();
                //进入时加载的动画
                enterAnimation();
                return true;
            }
        });
        //自定义控件中的接口监听  监听点击和下滑事件
        picture.setViewCall(new FriendCircleView.FriendCircleViewCall() {
            @Override
            public void viewDestry() {
                //加载退出动画
                exitAnimation(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                        getActivity().overridePendingTransition(0,0);
                    }
                });

//                Intent intent = new Intent(getActivity(),MainActivity.class);
//                startActivity(intent);
//                //取消activity跳转动画
//                getActivity().overridePendingTransition(0,0);
            }
        });
        return view;
    }

    /**
     * 退出动画
     * @param endAction
     */
    private void exitAnimation(final Runnable endAction) {
        //创建一个加速运动插值器对象
        TimeInterpolator sInterpolator = new AccelerateInterpolator();
        //控件设置动画、时间、X和Y方向上伸缩变化的比例、X和Y方向上平移的距离、设置插值器、设置结束后的操作
        picture.animate().setDuration(400).scaleX(widthScale).scaleY(heightScale).translationX(leftDelta).translationY(topDelta).setInterpolator(sInterpolator).withEndAction(endAction);
        //设置当前页面背景颜色由不透明变为透明
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(Color.BLACK,"alpha",0);
        bgAnim.setDuration(400);
        bgAnim.start();

    }

    /**
     * 进入动画
     */
    private void enterAnimation() {
        picture.setPivotX(0);
        picture.setPivotY(0);
        picture.setScaleX(widthScale);
        picture.setScaleY(heightScale);
        picture.setTranslationX(leftDelta);
        picture.setTranslationY(topDelta);
        //创建减速运动插值器
        TimeInterpolator sDecelerator = new DecelerateInterpolator();
        picture.animate().setDuration(400).scaleX(1).scaleY(1).translationX(0).translationY(0).setInterpolator(sDecelerator);
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(Color.BLACK,"alpha",0,255);
        bgAnim.setDuration(400);
        bgAnim.start();
    }

}
