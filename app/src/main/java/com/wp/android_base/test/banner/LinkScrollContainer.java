package com.wp.android_base.test.banner;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.wp.android_base.base.utils.log.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 2018/9/13.
 * <p>
 * Description:viewpager代理HorizontalScrollView的滑动事件
 */

public class LinkScrollContainer extends LinearLayout {

    private ViewPager mViewPager;
    private HorizontalScrollView mHorizontalScrollView;

    private float mDownX;
    private float mDownY;
    private int mTouchSlop;

    private List<View> mClickViews;

    public LinkScrollContainer(Context context) {
        super(context);
        init();
    }

    public LinkScrollContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinkScrollContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LinkScrollContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mClickViews = new ArrayList<>();
    }

    public void setLinkScroller(HorizontalScrollView horizontalScrollView, ViewPager viewPager) {
        this.mHorizontalScrollView = horizontalScrollView;
        this.mViewPager = viewPager;
    }

    public void addClickView(View v) {
        mClickViews.add(v);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Logger.e("Link>>", "dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isTouchPointInHScrollView(ev)) {
            //点击区域在HorizontalScrollView区域内，拦截事件，直接让ViewPager响应事件
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTouchPointInHScrollView(event)) {
            Logger.e("Link>>", "onTouchEvent");
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = event.getX();
                    mDownY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    float endX = event.getX();
                    float endY = event.getY();
                    if (Math.abs(endX - mDownX) <= mTouchSlop) {
                        hsvChildsPerformClick(endX, endY);
                    }
                    break;
            }
            mViewPager.onTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void hsvChildsPerformClick(float endX, float endY) {
        if (mClickViews != null && mClickViews.size() > 0) {
            for(int i=0;i<mClickViews.size();i++){
                View view = mClickViews.get(i);
                if(isTargetViewPerformClick(view,endX,endY)){
                    view.performClick();
                    break;
                }
            }
        }
    }

    private boolean isTargetViewPerformClick(View v,float endX, float endY) {
        if (v != null) {
            int[] location = new int[2];
            v.getLocationInWindow(location);
            int left = location[0];
            int top = location[1];
            int right = left + v.getMeasuredWidth();
            int bottom = top + v.getMeasuredHeight();
            int touchX = (int) ((endX + mDownX) / 2);
            int touchY = (int) ((endY + mDownY) / 2);
            if (touchX >= left && touchX <= right && touchY >= top && touchY <= bottom) {
                //perform click
                return true;
            }
        }
        return false;
    }

    private boolean isTouchPointInHScrollView(MotionEvent event) {
        if (mHorizontalScrollView == null) {
            return false;
        }
        float touchX = event.getRawX();
        float touchY = event.getRawY();
        int[] location = new int[2];
        mHorizontalScrollView.getLocationInWindow(location);
        int left = location[0];
        int top = location[1];
        int right = left + mHorizontalScrollView.getMeasuredWidth();
        int bottom = top + mHorizontalScrollView.getMeasuredHeight();
        if (touchX >= left && touchX <= right && touchY >= top && touchY <= bottom) {
            return true;
        }
        return false;
    }
}
