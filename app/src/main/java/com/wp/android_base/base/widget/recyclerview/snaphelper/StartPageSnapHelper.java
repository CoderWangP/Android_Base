package com.wp.android_base.base.widget.recyclerview.snaphelper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.view.View;

import com.wp.android_base.base.utils.log.Logger;


/**
 * Created by wangpeng on 2018/10/16.
 * <p>
 * Description:
 * <p>
 * calculateDistanceToFinalSnap	: 计算findSnapView方法返回的对应的ItemView当前的坐标与需要对齐的坐标之间的距离。
 * 返回一个int[2]数组，分别对应x轴和y轴方向上的距离
 * findSnapView	: 找到最接近对齐位置的view，该view称为SanpView，对应的position称为SnapPosition。
 * 如果返回null，就表示没有需要对齐的View，也就不会做滚动对齐调整
 * <p>
 * findTargetSnapPosition :	根据Fling操作的速率（参数velocityX和参数velocityY）找到需要滚动到的targetSnapPosition，
 * 该位置对应的View就是targetSnapView。如果找不到targetSnapPosition，就返回RecyclerView.NO_POSITION
 */


public class StartPageSnapHelper extends SnapHelper {

    private static final int MAX_SCROLL_ON_FLING_DURATION = 100; // ms
    private static final float MILLISECONDS_PER_INCH = 100f;

    private OrientationHelper mHorizontalHelper;
    private OrientationHelper mVerticalHelper;
    private RecyclerView mRecyclerView;


    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        this.mRecyclerView = recyclerView;
        super.attachToRecyclerView(recyclerView);
    }

    @Nullable
    @Override
    protected RecyclerView.SmoothScroller createScroller(RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(mRecyclerView.getContext()) {
            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state, RecyclerView.SmoothScroller.Action action) {
                int[] snapDistances = calculateDistanceToFinalSnap(mRecyclerView.getLayoutManager(),
                        targetView);
                final int dx = snapDistances[0];
                final int dy = snapDistances[1];
                final int time = calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)));
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator);
                }
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }

            @Override
            protected int calculateTimeForScrolling(int dx) {
                return Math.min(MAX_SCROLL_ON_FLING_DURATION, super.calculateTimeForScrolling(dx));
            }
        };
    }

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            //水平方向
            out[0] = distanceToStart(layoutManager, targetView, getHorizontalHelper(layoutManager));
            out[1] = 0;
        } else {
            //竖直方向
            out[0] = 0;
            out[1] = distanceToStart(layoutManager, targetView, getVerticalHelper(layoutManager));
        }
        return out;
    }

    private int distanceToStart(RecyclerView.LayoutManager layoutManager, View targetView, OrientationHelper horizontalHelper) {
        int childStart = horizontalHelper.getDecoratedStart(targetView);
        int containerStart = 0;
        if (layoutManager.getClipToPadding()) {
            containerStart = horizontalHelper.getStartAfterPadding();
        }
        return childStart - containerStart;
    }


    /**
     * 找到最接近（最合适，可能有一些定义的对齐规则）对齐位置的View
     *
     * @param layoutManager
     * @return
     */
    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        //当前attach到recyclerView的子视图数量
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
            if (firstPosition == RecyclerView.NO_POSITION) {
                return null;
            }
            int lastCompletePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            Logger.e("findSnapView>>", "lastCompletePosition= " + lastCompletePosition + "");
/*            if(lastCompletePosition == layoutManager.getItemCount() - 1){
                return null;
            }*/
            View firstView = linearLayoutManager.findViewByPosition(firstPosition);
            if (firstView == null) {
                return null;
            }
            OrientationHelper orientationHelper = null;
            if (layoutManager.canScrollHorizontally()) {
                orientationHelper = getHorizontalHelper(layoutManager);
            } else {
                orientationHelper = getVerticalHelper(layoutManager);
            }
            int start = Math.abs(orientationHelper.getDecoratedStart(firstView));
            if (start >= orientationHelper.getDecoratedMeasurement(firstView) / 2) {
                //如果第一个可见item的有超过一半不可见，则选择下个item为SnapView
                Logger.e("findSnapView>>", "firstPosition= " + (firstPosition + 1));
                return linearLayoutManager.findViewByPosition(firstPosition + 1);
            }
            Logger.e("findSnapView>>", "firstPosition= " + firstPosition + "");
            return firstView;
        }
        return null;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        //当前attach到recyclerView的子View数量
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return RecyclerView.NO_POSITION;
        }
        View startMostChild;
        if (layoutManager.canScrollHorizontally()) {
            startMostChild = findStartMostView(layoutManager, getHorizontalHelper(layoutManager));
        } else {
            startMostChild = findStartMostView(layoutManager, getVerticalHelper(layoutManager));
        }
        if (startMostChild == null) {
            return RecyclerView.NO_POSITION;
        }
        int startMostPosition = layoutManager.getPosition(startMostChild);
        if (startMostPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }
        boolean forwardDirection;
        Logger.e("findTargetSnapPosition>>", "velocityX = " + velocityX + "");
        Logger.e("findTargetSnapPosition>>", "velocityY = " + velocityY + "");
        if (layoutManager.canScrollHorizontally()) {
            forwardDirection = velocityX > 0;
        } else {
            forwardDirection = velocityY > 0;
        }
        Logger.e("findTargetSnapPosition>>", "startMostPosition= " + (forwardDirection ? startMostPosition + 1 : startMostPosition + ""));
        return forwardDirection ? startMostPosition + 1 : startMostPosition;
    }

    private View findStartMostView(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }
        View closestChild = null;
        int startest = Integer.MAX_VALUE;
        for (int i = 0; i < childCount; i++) {
            View childView = layoutManager.getChildAt(i);
            int childStart = helper.getDecoratedStart(childView);
            if (childStart < startest) {
                startest = childStart;
                closestChild = childView;
            }
        }
        return closestChild;
    }


    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null || this.mHorizontalHelper.getLayoutManager() != layoutManager) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }

    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        if (mVerticalHelper == null || mVerticalHelper.getLayoutManager() != layoutManager) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return mVerticalHelper;
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
}
