package com.wp.android_base.base.widget.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.wp.android_base.base.utils.log.Logger;

import java.lang.reflect.Field;

/**
 * 轮播显示器
 * Created by wangpeng on 2018/7/4.
 */
@SuppressLint("ClickableViewAccessibility")
public class ADViewPager extends ViewPager {

    public static final String TAG = "ADViewPager";

    private SimpleOnPageChangeListener mSimpleOnPageChangeListener;
    private int delayMillis;
    private Handler handler;

    private boolean isPlaying = false;
    private boolean isScrolling = false;
    private boolean isCanScroll = true;
    private ScrollerCustomDuration mScroller = null;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying) {
                handler.postDelayed(runnable, delayMillis);
                if (!isScrolling) {
                    Logger.e(TAG, "position= " + ADViewPager.this.getCurrentItem());
                    ADViewPager.this.setCurrentItem(ADViewPager.this.getCurrentItem() + 1);
                }
            }
        }
    };

    public ADViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ADViewPager(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        handler = new Handler();
        //设置PageTransformer时，注意设置OffScreenPageLimit，不然有bug
        setOffscreenPageLimit(2);
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            mScroller = new ScrollerCustomDuration(getContext(), new AccelerateDecelerateInterpolator());
            scroller.set(this, mScroller);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始循环播放
     *
     * @param delayMillis
     */
    public void play(int delayMillis) {
        PagerAdapter pagerAdapter = getAdapter();
        if (pagerAdapter != null && pagerAdapter.getCount() > 1) {
            isCanScroll = true;
            this.delayMillis = delayMillis;
            handler.removeCallbacks(runnable);
            isPlaying = true;
            handler.postDelayed(runnable, delayMillis);
        } else {
            isCanScroll = false;
        }
    }

    /**
     * 设置起始显示item
     */
    public void setStartItem() {
        PagerAdapter pagerAdapter = getAdapter();
        if (pagerAdapter != null && pagerAdapter.getCount() > 1) {
            setCurrentItem(pagerAdapter.getCount() / 2);
        } else {
            setCurrentItem(0);
        }
    }

    /**
     * 是否在播放中
     *
     * @return
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 停止播放
     */
    public void stop() {
        isPlaying = false;
        handler.removeCallbacks(runnable);
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (isCanScroll) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // ViewParent viewParent = getParent();
        switch (arg0.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // if (viewParent != null)
                // {
                // viewParent.requestDisallowInterceptTouchEvent(false);
                // }
                play(delayMillis);
                break;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                // if (viewParent != null)
                // {
                // viewParent.requestDisallowInterceptTouchEvent(true);
                // }
                stop();
                break;

            default:
                break;
        }
        return super.onTouchEvent(arg0);
    }

    public void addPageChangeListener(SimpleOnPageChangeListener simpleOnPageChangeListener){
        this.mSimpleOnPageChangeListener = simpleOnPageChangeListener;
        addOnPageChangeListener(new MyOnPageChangeListener());
    }

    public static abstract class SimpleOnPageChangeListener{

        public void onPageSelected(int position) {
        }
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Logger.e(TAG,"position = " + position,"positionOffset = " + positionOffset,"positionOffsetPixels = " + positionOffsetPixels);
        }
        public void onPageScrollStateChanged(int state) {
        }
    }

    /**
     * PageChangeListener
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            if(mSimpleOnPageChangeListener != null){
                mSimpleOnPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(mSimpleOnPageChangeListener != null){
                mSimpleOnPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            switch (state) {
                case SCROLL_STATE_IDLE:
                    isScrolling = false;
                    break;
                default:
                    isScrolling = true;
                    break;
            }

            if(mSimpleOnPageChangeListener != null){
                mSimpleOnPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }


    public class ScrollerCustomDuration extends Scroller {

        public ScrollerCustomDuration(Context context) {
            super(context);
        }

        public ScrollerCustomDuration(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 300);
        }
    }

}
