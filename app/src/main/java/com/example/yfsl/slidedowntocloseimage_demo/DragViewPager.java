package com.example.yfsl.slidedowntocloseimage_demo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nineoldandroids.view.ViewHelper;

public class DragViewPager extends ViewPager implements View.OnClickListener {
    public static final int STATUS_NORMAL = 0;//正常浏览状态
    public static final int STATUS_MOVING = 0;//滑动状态
    public static final int STATUS_RESETING = 0;//返回中状态
    public static final String TAG = "DragViewPager";

    public static final float MIN_SCALE_SIZE = 0.3f;
    public static final int BACK_DURATION = 300;//动画的时间  ms
    public static final int DRAF_GAP_PX = 50;

    private int currentStatus = STATUS_NORMAL;
    private int currentPageStatus ;

    private float mDownX;
    private float mDownY;
    private float screenHeight;

    //要缩放的View
    private View currentShowView;
    //速度检测类
    private VelocityTracker mVelocityTracker;
    private IAnimClose iAnimClose;


    public DragViewPager(@NonNull Context context) {
        super(context);
    }

    public DragViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        screenHeight = ScreenUtils.getScreenHeight(context);
        setBackgroundColor(Color.BLACK);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                currentPageStatus = i;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getAdapter() instanceof ImagePagerAdapter){
            ImagePagerAdapter adapter = (ImagePagerAdapter) getAdapter();
            SubsamplingScaleImageView mImage = adapter.getItem(getCurrentItem()).getView().findViewById(R.id.picture);
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    Log.e("TAG","onInterceptTouchEvent:ACTION_DOWN");
                    mDownX = ev.getRawX();
                    mDownY = ev.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e("TAG","onInterceptTouchEvent:ACTION_MOVE");
                    if (mImage.getCenter() != null && mImage.getCenter().y <= mImage.getHeight()/mImage.getScale()/2){
                        int deltax = Math.abs((int)(ev.getRawX() - mDownX));
                        int deltay = (int)(ev.getRawY() - mDownY);
                        if (deltay > DRAF_GAP_PX && deltax <= DRAF_GAP_PX){//往下移动超过临界，左右移动不超过临界时，拦截滑动事件
                            return true;
                        }
                    }
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (currentStatus == STATUS_RESETING) return false;
        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                addIntoVelocity(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                addIntoVelocity(ev);
                int deltay = (int)(ev.getRawY() - mDownY);
                if (deltay <= DRAF_GAP_PX && currentStatus != STATUS_MOVING){
                    return super.onTouchEvent(ev);
                }
                if (currentPageStatus != SCROLL_STATE_DRAGGING && (deltay > DRAF_GAP_PX || currentStatus == STATUS_MOVING)){
                    moveView(ev.getRawX(),ev.getRawY());
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (currentStatus != STATUS_MOVING){
                    return super.onTouchEvent(ev);
                }
                final float mUpX = ev.getRawX();
                final float mUpY = ev.getRawY();
                float vY = computeYVelocity();
                if (vY > 1200 || Math.abs(mUpY - mDownY) > screenHeight/4){//下滑速度超过1200，或者下滑距离超过屏幕高度的一半，就关闭
                    if (iAnimClose != null){
                        iAnimClose.onPictureRelease(currentShowView);
                    }
                }else {
                    resetReviewState(mUpX,mUpY);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private float computeYVelocity() {
        float result = 0;
        if (mVelocityTracker != null){
            mVelocityTracker.computeCurrentVelocity(1000);
            result = mVelocityTracker.getYVelocity();
            releaseVelocity();
        }
        return result;
    }

    private void releaseVelocity() {
        if (mVelocityTracker != null){
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void addIntoVelocity(MotionEvent ev) {
        if (mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
    }

    /**
     * 返回浏览状态
     * @param mUpX
     * @param mUpY
     */
    private void resetReviewState(final float mUpX, final float mUpY) {
        currentStatus = STATUS_RESETING;
        if (mUpY != mDownY){
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mUpY,mDownY);
            valueAnimator.setDuration(BACK_DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float mY = (float)animation.getAnimatedValue();
                    float percent = (mY - mDownY)/(mUpY - mDownY);
                    float mX = percent*(mUpX - mDownX) + mDownX;
                    moveView(mX,mY);
                    if (mY == mDownY){
                        mDownX = 0;
                        mDownY = 0;
                        currentStatus = STATUS_NORMAL;
                    }
                }
            });
            valueAnimator.start();
        }else if (mUpX != mDownX){
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mUpX,mDownY);
            valueAnimator.setDuration(BACK_DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float mX = (float)animation.getAnimatedValue();
                    float percent = (mX - mDownX)/(mUpX - mDownX);
                    float mY = percent*(mUpY - mDownY) + mDownY;
                    moveView(mX,mY);
                    if (mX == mDownX){
                        mDownX = 0;
                        mDownY = 0;
                        currentStatus = STATUS_NORMAL;
                    }
                }
            });
            valueAnimator.start();
        }else if (iAnimClose != null){
            iAnimClose.onPictureClick();
        }
    }

    private void moveView(float mX, float mY) {
        if (currentShowView == null) return;
        currentStatus = STATUS_MOVING;
        float deltaX = mX - mDownX;
        float deltaY = mY - mDownY;
        float scale = 1f;
        float alphaPercent = 1f;
        if (deltaY > 0 ){
            scale = 1 - Math.abs(deltaY) / screenHeight;
            alphaPercent = 1 - Math.abs(deltaY) / (screenHeight/2);
        }
        ViewHelper.setTranslationX(currentShowView,deltaX);
        ViewHelper.setTranslationY(currentShowView,deltaY);
        scaleView(scale);
        setBackgroundColor(getBlackAlpha(alphaPercent));
    }

    private int getBlackAlpha(float alphaPercent) {
        alphaPercent = Math.min(1,Math.max(0,alphaPercent));
        int intAlpha = (int)(alphaPercent*255);
        return Color.argb(intAlpha,0,0,0);
    }

    /**
     * 缩放view
     * @param scale
     */
    private void scaleView(float scale) {
        scale = Math.min(Math.max(scale,MIN_SCALE_SIZE),1);
        ViewHelper.setScaleX(currentShowView,scale);
        ViewHelper.setScaleY(currentShowView,scale);
    }

    @Override
    public void onClick(View v) {
        if (iAnimClose != null){
            iAnimClose.onPictureClick();
        }
    }

    public void setCurrentShowView(View view) {
        this.currentShowView = view;
        if (currentShowView != null){
            currentShowView.setOnClickListener(this);
        }
    }

    public interface IAnimClose{
        void onPictureClick();

        void onPictureRelease(View view);
    }
}
