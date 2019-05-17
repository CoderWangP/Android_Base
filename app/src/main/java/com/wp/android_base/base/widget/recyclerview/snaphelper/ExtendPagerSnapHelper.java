package com.wp.android_base.base.widget.recyclerview.snaphelper;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.wp.android_base.base.utils.log.Logger;


/**
 * Created by wangpeng on 2018/10/16.
 * <p>
 * Description:扩展的PagerSnapHelper
 */

public class ExtendPagerSnapHelper extends PagerSnapHelper{

    private RecyclerView mRecyclerView;

    private final RecyclerView.OnScrollListener mScrollListener =
            new RecyclerView.OnScrollListener() {
                boolean mScrolled = false;
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && mScrolled) {
                        mScrolled = false;
                        if(mOnPageChangeListener != null){
                            mOnPageChangeListener.onPageSelected(getCurrentItem());
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dx != 0 || dy != 0) {
                        mScrolled = true;
                        if(mOnPageChangeListener == null || mRecyclerView == null || mRecyclerView.getLayoutManager() == null){
                            return;
                        }
                        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                        if(layoutManager instanceof LinearLayoutManager){
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                            Logger.e("onScrolled>>","currentItem =" + getCurrentItem() ,"dx=" + dx,"calculateDistance = " + calculateDistanceToFinalSnap(linearLayoutManager,findSnapView(linearLayoutManager))[0]);
                            if(linearLayoutManager.canScrollHorizontally()){
                                mOnPageChangeListener.onPageScrolled(getCurrentItem(),0,dx);
                            }else if(linearLayoutManager.canScrollVertically()){
                                mOnPageChangeListener.onPageScrolled(getCurrentItem(),0,dy);
                            }
                        }
                    }
                }
            };

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        if(mRecyclerView == null){
            return;
        }
        if (mRecyclerView == recyclerView) {
            return;
        }
        this.mRecyclerView = recyclerView;
        mRecyclerView.removeOnScrollListener(mScrollListener);
        mRecyclerView.addOnScrollListener(mScrollListener);

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

    public int getCurrentItem() {
        if (mRecyclerView != null) {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            return layoutManager.getPosition(findSnapView(layoutManager));
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
}
