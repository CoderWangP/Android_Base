package com.wp.android_base.test.banner;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.wp.android_base.base.utils.ScreenUtil;

import java.lang.reflect.Field;

/**
 * Created by wangpeng on 2018/9/11.
 * <p>
 * Description:
 */

public class MyHScrollView extends HorizontalScrollView{

    private int mTabCount = 0;
    private LinearLayout mTabContainer = null;
    private int mDownX = 0;
    private int mCurrentPage = 0;

    private int HOS_ITEM_WIDTH = ScreenUtil.getScreenWidth() - ScreenUtil.dp2px(14) - ScreenUtil.dp2px(28);

    private VelocityTracker mVelocityTracker;
    //fling操作的最小速率
    private float MIN_FLING_VELOCITY = 600f;


    public MyHScrollView(Context context) {
        super(context);
    }

    public MyHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyHScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyHScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTabContainer = (LinearLayout) getChildAt(0);
        mTabCount = mTabContainer.getChildCount();
    }

    private void init() {
        try {
            Class<?>  horizontalScrollViewClass = HorizontalScrollView.class;
            Field scroller = horizontalScrollViewClass.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            OverScroller overScroller = new MyOverScroll(getContext(),new AccelerateDecelerateInterpolator());
            scroller.set(this,overScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentPage(int currentPage) {
        this.mCurrentPage = currentPage;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityX = mVelocityTracker.getXVelocity();
                if(Math.abs(velocityX) >= MIN_FLING_VELOCITY){
                    fling2Target(velocityX);
                }else{
                    if (Math.abs(ev.getX() - mDownX) > HOS_ITEM_WIDTH / 2) {
                        if (ev.getX() - mDownX > 0) {
                            smoothScrollToPrePage();
                        } else {
                            smoothScrollToNextPage();
                        }
                    } else {
                        smoothScrollToCurrent();
                    }
                }
                if(mVelocityTracker != null){
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }


    private void fling2Target(float velocityX) {
        if(velocityX > 0){
            smoothScrollToPrePage();
        }else{
            smoothScrollToNextPage();
        }
    }

    private void smoothScrollToCurrent() {
        smoothScrollTo(mCurrentPage * HOS_ITEM_WIDTH,0);
    }

    private void smoothScrollToNextPage() {
        if(mCurrentPage < mTabCount - 1){
            mCurrentPage++;
            smoothScrollTo(mCurrentPage * HOS_ITEM_WIDTH,0);
        }
    }

    private void smoothScrollToPrePage() {
        if(mCurrentPage > 0){
            mCurrentPage--;
            smoothScrollTo(mCurrentPage * HOS_ITEM_WIDTH,0);
        }
    }

    //设置接口
    public interface OnScrollListener{
        void onScroll(int scrollX);
    }

    private OnScrollListener mOnScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.mOnScrollListener = onScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollListener != null){
            mOnScrollListener.onScroll(l);
        }
    }


    public class MyOverScroll extends OverScroller{

        public MyOverScroll(Context context) {
            super(context);
        }

        public MyOverScroll(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 300);
        }
    }
}
