package com.wp.android_base.base.widget.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wp on 2019/4/8.
 * <p>
 * Description:主界面用的ViewPager
 */

public class MainTabViewPager extends ViewPager{

    private boolean mIsCanScroll = false;

    public MainTabViewPager(@NonNull Context context) {
        super(context);
    }

    public MainTabViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item,false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!mIsCanScroll){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!mIsCanScroll){
            return false;
        }
        return super.onTouchEvent(ev);
    }


    public void setIsCanScroll(boolean isCanScroll) {
        this.mIsCanScroll = isCanScroll;
    }
}
