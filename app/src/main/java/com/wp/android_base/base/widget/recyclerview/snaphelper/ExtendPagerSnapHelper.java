package com.wp.android_base.base.widget.recyclerview.snaphelper;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;


import com.wp.android_base.base.utils.log.Logger;



/**
 * Created by wangpeng on 2018/10/16.
 * <p>
 * Description:扩展的PagerSnapHelper
 */

public class ExtendPagerSnapHelper extends PagerSnapHelper{

    private static final String TAG = "ExtendPagerSnapHelper";

    private RecyclerView mRecyclerView;

    private Handler mHandler;
    private Runnable mRunnable;
    private boolean canScroll = true;
    private boolean isPlaying = false;

    private int delayMillis = 4 * 1000;

    private final RecyclerView.OnScrollListener mScrollListener =
            new RecyclerView.OnScrollListener() {
                boolean scrolled = false;
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && scrolled) {
                        scrolled = false;
                        if(mOnPageChangeListener != null){
                            mOnPageChangeListener.onPageSelected(getCurrentItem());
                        }
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    if (dx != 0 || dy != 0) {
                        scrolled = true;
                        if(mOnPageChangeListener == null || mRecyclerView == null || mRecyclerView.getLayoutManager() == null){
                            return;
                        }
                        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                        if(layoutManager instanceof LinearLayoutManager){
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                            Logger.d("onScrolled>>","currentItem =" + getCurrentItem() ,"dx=" + dx/*,"calculateDistance = " + calculateDistanceToFinalSnap(linearLayoutManager,findSnapView(linearLayoutManager))[0]*/);
                            if(linearLayoutManager.canScrollHorizontally()){
                                mOnPageChangeListener.onPageScrolled(getCurrentItem(),0,dx);
                            }else if(linearLayoutManager.canScrollVertically()){
                                mOnPageChangeListener.onPageScrolled(getCurrentItem(),0,dy);
                            }
                        }
                    }
                }
            };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        if(recyclerView == null){
            return;
        }
        if (mRecyclerView == recyclerView) {
            return;
        }
        this.mRecyclerView = recyclerView;
        mRecyclerView.removeOnScrollListener(mScrollListener);
        mRecyclerView.addOnScrollListener(mScrollListener);
        if(canScroll){
            mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            start();
                            break;
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            stop();
                            break;
                    }
                    return false;
                }
            });
        }

        super.attachToRecyclerView(recyclerView);
    }

    public void setCurrentItem(int position){
        setCurrentItem(position,false);
    }

    public void setCurrentItem(int position,boolean smoothScroll){
        if(mRecyclerView != null){
            if(smoothScroll){
                mRecyclerView.smoothScrollToPosition(position);
            }else{
                scrollToPosition(position);
            }
        }
    }

    private void scrollToPosition(int position) {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if(layoutManager == null){
            return;
        }
        if(layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            linearLayoutManager.scrollToPositionWithOffset(position,0);
        }
    }

    private int getCurrentItem() {
        if (mRecyclerView != null) {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if(layoutManager == null){
                return RecyclerView.NO_POSITION;
            }
            View currentView = findSnapView(layoutManager);
            if(currentView == null){
                return RecyclerView.NO_POSITION;
            }
            return layoutManager.getPosition(currentView);
        }
        return RecyclerView.NO_POSITION;
    }

    public interface OnPageChangeListener{
        void onPageSelected(int position);
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
    }

    private OnPageChangeListener mOnPageChangeListener;

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener){
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void start(){
        Logger.d(TAG,"start");
        if(mHandler == null){
            mHandler = new Handler();
        }
        if(mRunnable == null){
            mRunnable = () -> {
                mHandler.postDelayed(mRunnable,delayMillis);
                setCurrentItem(getCurrentItem() + 1,true);
            };
        }
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(mRunnable,delayMillis);
        isPlaying = true;
    }

    public void stop(){
        Logger.d(TAG,"stop");
        if(!isPlaying){
            return;
        }
        isPlaying = false;
        if(mHandler != null && mRunnable != null){
            mHandler.removeCallbacks(mRunnable);
        }
    }
}
