package com.wp.android_base.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

import com.wp.android_base.R;
import com.wp.android_base.base.utils.log.Logger;

/**
 * Created by wangpeng on 2018/11/20.
 * <p>
 * Description:嵌套滚动，上半部是一个View，下半部是一个带滚轮的View
 * <p>
 * <p>
 * VelocityTracker:一些方法的解释：
 * 追踪触摸事件的速率，用于实现fling和其他手势的帮助类
 * 1.通过VelocityTracker.obtain()来获取实例,检测结束时，要确保调用了recycle()方法回收
 * 2.添加一个移动事件到追踪器，通过VelocityTracker.addMovement(event)传入你需要追踪的touch事件，
 * <p>
 * 一般在ACTION_DOWN时调用添加addMovement(event)，然后在ACTION_MOVE和ACTION_UP等action中接收
 * 1.在MotionEvent的ACTION_DOWN调用addMovement()
 * 2.接下来，在MotionEvent的ACTION_MOVE和ACTION_UP等动作中接收。
 * 不管是哪一个events都可以调用本方法
 * <p>
 * 3.当要确定速度时调用computeCurrentVelocity(int units)
 * units：速度单位，px/ms或者1000px/s
 * 值为1时：代表每毫秒运动一个像素，px/ms
 * 值为1000时：代表每秒运动1000个像素，1000px/s
 * <p>
 * 4.getXVelocity(),getYVelocity()获取追踪到的X,Y轴的速度
 * <p>
 * 5.recycle()，方法，回收VelocityTracker，以供其他事件使用，可以在获取速度等参数后调用
 *
 *
 * 1.View.startScroll(getScroolX(), getScrollY(), -50, -50, duration):
 *   从当前的ScrollX,ScrollY滚动到另一个位置
 *
 * 2.View.fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY):
 *   Start scrolling based on a fling gesture
 *   基于手势继续滑动，松开手指后，根据手势的速率继续滚动
 *
 *
 * getScrollX,getScrollY, getXVelocity,getYVelocity：方向正负的问题，可以看/rootProject/笔记/ScrollerX_Y解释
 *
 *
 */

public class NestedScrollLayout extends ViewGroup {

    private static final String TAG = "NestedScrollLayout";

    private int mTopViewId;
    private int mScrollViewId;

    private View mHeaderView;
    private View mScrollView;

    /**
     * 有滚轮View的初始偏移（一般设置为HeaderView的高度）
     */
    private int mScrollInitOffset;
    /**
     * 有滚轮View的结束位置偏移（一般设置为HeaderView刚刚向上隐藏的位置）
     */
    private int mScrollEndOffset;
    /**
     * 有滚轮View的当前偏移（初始为{@link #mScrollInitOffset}）
     */
    private int mScrollCurrentOffset = -1;

    /**
     * 最小fling速度
     */
    private int mMinVelocity;

    /**
     * 最大接受的fling速度
     */
    private int mMaxVelocity;

    /**
     * 控制滚动的Scroller
     */
    private OverScroller mOverScroller;

    /**
     * down事件x,事件发生的初始x
     */
    private float mInitialDownX;
    /**
     * down事件y,事件发生的初始y
     */
    private float mInitialDownY;

    /**
     * 在认为正在滚动之前，触摸可以移动的像素距离
     * (当手指在屏幕上滑动时，如果两次滑动之间的距离小于这个常量，那么系统就不认为你是在进行滑动操作)
     */
    private float mTouchSlop;

    /**
     * 是否拦截事件
     */
    private boolean mIsIntercepting;

    /**
     * 速率追踪
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 上次事件的Y坐标
     */
    private float mLastMotionY;


    public NestedScrollLayout(@NonNull Context context) {
        this(context, null);
    }

    public NestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        Logger.e(TAG, "init");
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NestedScrollLayout);
        mTopViewId = array.getResourceId(R.styleable.NestedScrollLayout_top_view_id, 0);
        mScrollViewId = array.getResourceId(R.styleable.NestedScrollLayout_scroll_view_id, 0);
        array.recycle();

        //允许改变绘制顺序
        setChildrenDrawingOrderEnabled(true);

        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mTouchSlop = viewConfiguration.getScaledTouchSlop();

        mOverScroller = new OverScroller(context);
    }

    /**
     * 改变绘制顺序，默认ViewGroup的绘制顺序是xml布局中的控件从上到下的顺序
     * 保证绘制顺序，先绘制Header，再绘制Scroll
     *
     * @param childCount
     * @param i
     * @return
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mHeaderView == null || mScrollView == null) {
            return i;
        }
        int headerIndex = indexOfChild(mHeaderView);
        int scrollIndex = indexOfChild(mScrollView);
        if (headerIndex < scrollIndex) {
            return i;
        }
        if (i == headerIndex) {
            return scrollIndex;
        } else if (i == scrollIndex) {
            return headerIndex;
        }
        return i;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Logger.e(TAG, "onFinishInflate");
        if (mTopViewId != 0) {
            mHeaderView = findViewById(mTopViewId);
        }
        if (mScrollViewId != 0) {
            mScrollView = findViewById(mScrollViewId);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Logger.e(TAG, "onMeasure");

//        measureChildren(widthMeasureSpec,heightMeasureSpec);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (mHeaderView != null) {
            int headerHeight = mHeaderView.getMeasuredHeight();
            mScrollInitOffset = top + headerHeight;
            if (headerHeight > 0 && mScrollCurrentOffset == -1) {
                mScrollCurrentOffset = mScrollInitOffset;
            }
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
        }

        if (mScrollView != null) {
//            int scrollWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width - left - right, MeasureSpec.EXACTLY);
            int scrollHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - top - bottom, MeasureSpec.EXACTLY);
            mScrollView.measure(widthMeasureSpec, scrollHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Logger.e(TAG, "onLayout");
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();
        if (mHeaderView != null) {
            int headerWidth = mHeaderView.getMeasuredWidth();
            int headerHeight = mHeaderView.getMeasuredHeight();
            mHeaderView.layout(left, top, left + headerWidth, top + headerHeight);
        }
        if (mScrollView != null) {
            int height = getMeasuredHeight() - top - bottom;
            int scrollWidth = mScrollView.getMeasuredWidth();
            int scrollHeight = mScrollView.getMeasuredHeight();
            mScrollView.layout(left, mScrollCurrentOffset, left + scrollWidth, mScrollCurrentOffset + height);
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // 去掉默认行为，使得每个事件都会经过这个Layout
    }

    /**
     * 可以先看下{@link android.support.v4.widget.SwipeRefreshLayout#onInterceptTouchEvent(MotionEvent)}
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Logger.e(TAG + ">>>onInterceptTouchEvent","event=" + ev.getAction());
        /**
         * 上下运动进行拦截
         */
        if (!isEnabled() || mHeaderView == null || mScrollView == null) {
            //不拦截
            Logger.e(TAG, "onInterceptTouchEvent->canScrollUp=" + canScrollUp());
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsIntercepting = false;
                mInitialDownX = ev.getX();
                mInitialDownY = ev.getY();
                /**
                 * 两次滑动之间的距离，必须大于mTouchSlop才认为是Move事件
                 */
                mLastMotionY = mInitialDownY + mTouchSlop;
                break;

            /**
             * Move事件一断被拦截，那么这个Move事件就会传递给本NestedScrollLayout的{@link #onTouchEvent(MotionEvent)}
             * 的Move事件处理，直到事件被打断，在这个过程中的后续Move事件都是{@link #onTouchEvent(MotionEvent)}的Move事件在处理
             */
            case MotionEvent.ACTION_MOVE:
                float x = ev.getX();
                float y = ev.getY();
                float deltaX = x - mInitialDownX;
                float deltaY = y - mInitialDownY;
                Logger.e(TAG + ">>onInterceptTouchEvent>>",
                        "Move事件被调用",
                        "canScrollUp = " + canScrollUp(),
                        "mIsIntercepting = " + mIsIntercepting,
                        "mScrollCurrentOffset = " + mScrollCurrentOffset,
                        "mScrollEndOffset = " + mScrollEndOffset,
                        "(Math.abs(deltaY) > mTouchSlop) = " + (Math.abs(deltaY) > mTouchSlop));
                /**
                 * !mIsIntercepting: 如果已经拦截，接下来的Move事件会传递给自己onTouchEvent的Move事件，自己的onTouchEvent的Move事件会一直调用直到Move事件结束,就是说onInterceptTouchEvent调用一次拦截，而onTouchEvent的Move事件会继续Move事件的后续调用，可能调用多次，
                 * Math.abs(deltaY) > Math.abs(deltaX): 只拦截上下方向的事件
                 * Math.abs(deltaY) > mTouchSlop: 两次滑动距离超过TouchSlop时(即Math.abs(y - mDownY) > TouchSlop),系统才会认为是滑动，这时，滑动距离大于TouchSlop,
                 * mLastMotionY = mInitialDownY + mTouchSlop: 为了让滑动更平滑，缩小滑动的距离，就相当于改变初始的mInitialDownY;
                 */
                if (!mIsIntercepting/*如果未拦截*/ ) {
                    if(Math.abs(deltaY) > Math.abs(deltaX) && Math.abs(deltaY) > mTouchSlop){
                        if(!canScrollUp()/*带Scroll的View已经滑动到顶端*/){
                            if(deltaY > 0 && mScrollCurrentOffset < mScrollInitOffset || deltaY < 0 && mScrollCurrentOffset > mScrollEndOffset){
                                Logger.e(TAG + ">>onInterceptTouchEvent>>","Move事件被拦截前","mLastMotionY = " + mLastMotionY);
//                                mLastMotionY = mInitialDownY + mTouchSlop;
                                Logger.e(TAG + ">>onInterceptTouchEvent>>","Move事件被拦截后","mLastMotionY = " + mLastMotionY);
                                mIsIntercepting = true;
                                return true;
                            }
                        }
                    }
                    /**
                     * 一断开始未拦截，后续又拦截(如先将HeaderView刚好滑完，抬起手指，下次接着滑动)，
                     * 这时候Move事件具体滑动到了哪个点不知道，这里就必须记录下Move事件运行后的Y，以便后续拦截到Move事件，
                     * 本NestedScrollLayout的{@link #onTouchEvent(MotionEvent)}找到上一次滑动的点Y，再根据本次的y，算出offset，进行平滑移动
                     */
                    mLastMotionY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsIntercepting = false;
                break;
        }
        return mIsIntercepting;
    }

    /**
     * 可以先看下{@link android.support.v4.widget.SwipeRefreshLayout#onTouchEvent(MotionEvent)}
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || mHeaderView == null || mScrollView == null) {
            Logger.e(TAG, "onTouchEvent->canScrollUp=" + canScrollUp());
            return false;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mIsIntercepting = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float deltaX = x - mInitialDownX;
                float deltaY = y - mInitialDownY;
                Logger.e(TAG + ">>onTouchEvent>>",
                        "Move事件被调用",
                        "mScrollCurrentOffset = " + mScrollCurrentOffset,
                        "mScrollEndOffset = " + mScrollEndOffset,
                        "(Math.abs(deltaY) > mTouchSlop) = " + (Math.abs(deltaY) > mTouchSlop));

                if (!mIsIntercepting/*如果未拦截*/ ) {
                    if(Math.abs(deltaY) > Math.abs(deltaX) && Math.abs(deltaY) > mTouchSlop){
                        if(!canScrollUp()/*带Scroll的View已经滑动到顶端*/){
                            if(deltaY > 0 && mScrollCurrentOffset < mScrollInitOffset || deltaY < 0 && mScrollCurrentOffset > mScrollEndOffset){
//                                mLastMotionY = mInitialDownY + mTouchSlop;
                                mIsIntercepting = true;
                            }
                        }
                    }
                }

                if (mIsIntercepting) {
                    //注意这里要换成整数去计算，不然mScrollCurrentOffset，mScrollCurrentOffset都为整数，
                    // 而且平移ViewCompat.offsetTopAndBottom方法的参数接收整数，不转为整数，平移精度，与计算精度，存在误差，无法对齐
                    int offsetY = (int) (y - mLastMotionY);
                    boolean isResetDispatchEvent = move(offsetY);
                    if(isResetDispatchEvent){
                        // 重新dispatch一次down事件，使得列表可以继续滚动
                        // (本次Move事件被拦截，而且已经滑动到了临界值，需要下面的ScrollView继续滚动，
                        // 这时需要重新dispatch一次事件，重新分发，根据临界值重新判断要不要拦截事件，这样就可以使下方的
                        // ScrollView继续滑动)
                        int oldAction = event.getAction();
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                        event.setAction(oldAction);
                    }
                    mLastMotionY = y;
                }

                break;
            case MotionEvent.ACTION_UP:
                if(mIsIntercepting){
                    mIsIntercepting = false;
                    mVelocityTracker.computeCurrentVelocity(1000,mMaxVelocity);
                    int yVelocity = (int) mVelocityTracker.getYVelocity();

                    Logger.e(TAG + ">>onTouchEvent>>",
                            "ACTION_UP事件被调用",
                            "yVelocity = " + yVelocity,
                            "mMinVelocity = " + mMinVelocity,
                            "mScrollCurrentOffset = " + mScrollCurrentOffset);

/*                    if(Math.abs(yVelocity) > mMinVelocity){
                        *//**
                         * fling操作{@link android.widget.OverScroller#fling(int, int, int, int, int, int, int, int, int, int)}
                         *//*
                        if(yVelocity > 0){
                            //手指从下往上滑动
                            mOverScroller.fling(0,mScrollCurrentOffset,0,yVelocity,0,0,mScrollEndOffset,Integer.MAX_VALUE,0,0);
                        }else{
                            mOverScroller.fling(0,mScrollCurrentOffset,0,yVelocity,0,0,mScrollInitOffset,Integer.MAX_VALUE,0,0);
                        }
                    }else{*/
                        /**
                         * 缓慢滑动到指定位置{@link android.widget.OverScroller#startScroll(int, int, int, int)}}
                         */
                        if(yVelocity > 0){
                            //手指从下往上滑动
                            mOverScroller.startScroll(0,mScrollCurrentOffset,0,mScrollEndOffset - mScrollCurrentOffset,Math.abs(mScrollEndOffset - mScrollCurrentOffset) * 2);
                        }else if(yVelocity < 0){
                            mOverScroller.startScroll(0,mScrollCurrentOffset,0,mScrollInitOffset - mScrollCurrentOffset,Math.abs(mScrollInitOffset - mScrollCurrentOffset) * 2);
                        }else{
                            //y轴上没有速度
                            if(mScrollCurrentOffset < mScrollInitOffset / 2){
                                //向上收起
                                mOverScroller.startScroll(0,-mScrollCurrentOffset,0,mScrollEndOffset - mScrollCurrentOffset,Math.abs(mScrollEndOffset - mScrollCurrentOffset) * 2);
                            }else{
                                //向下撑开
                                mOverScroller.startScroll(0,mScrollCurrentOffset,0,mScrollInitOffset - mScrollCurrentOffset,Math.abs(mScrollInitOffset - mScrollCurrentOffset) * 2);
                            }
                        }
                   // }
                    postInvalidate();
                }
                releaseTracker();
                return false;
            case MotionEvent.ACTION_CANCEL:
                if(mIsIntercepting){
                    mIsIntercepting = false;
                }
                releaseTracker();
                return false;
        }
        return true;
    }

    /**
     * 回收速率跟踪器
     */
    private void releaseTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }

    }


    private boolean move(int offsetY) {
        //拖动距离在mScrollEndOffset与mScrollInitOffset之间变化
        boolean isResetDispatchEvent = false;//到极限值时，重新Dispatch一次事件，让ScrollView可以继续滚动
        if (mScrollCurrentOffset + offsetY <= mScrollEndOffset) {
            //向上到达极限值，修正offsetY
            offsetY = mScrollEndOffset - mScrollCurrentOffset;
            isResetDispatchEvent = true;
        }else if(mScrollCurrentOffset + offsetY > mScrollInitOffset){
            //向下到达极限值,修正offsetY
            offsetY = mScrollInitOffset - mScrollCurrentOffset;
        }
        ViewCompat.offsetTopAndBottom(mHeaderView,offsetY);
        int scrollOffsetY = offsetY;
        ViewCompat.offsetTopAndBottom(mScrollView,scrollOffsetY);
        mScrollCurrentOffset += scrollOffsetY;
        Logger.e(TAG + ">>ACTION_MOVE>>","mScrollCurrentOffset=" + mScrollCurrentOffset,
                "offsetY=" + offsetY,"isReDispatchEvent=" + isResetDispatchEvent,"mLastMotionY=" + mLastMotionY);
        return isResetDispatchEvent;
    }


    /**
     * invalidate：在UI线程中调用
     * postInvalidate：在工作线程中调用，内部是用了Handler，向主线程发送Message，主线程处理Message时，调用了invalidate
     *
     */
    @Override
    public void computeScroll() {
        if(mOverScroller.computeScrollOffset()){
            move(mOverScroller.getCurrY());
            //滚动未结束，动画效果，一点点绘制,最终还是调用View的scrollTo去滚动屏幕
//            scrollTo(mOverScroller.getCurrX(),mOverScroller.getCurrY());
            //重新绘制,刷新View
            postInvalidate();
        }
    }

    /**
     * 在竖直方向上是否还能向上移动，如ListView滚动到顶部，不能再向上滚动了，就会返回false
     * 就是说，ListView或者RecyclerView已经滚动到了第一个Item且完全显示,上面没有Item了，不能再向上滚动
     *
     * canScrollVertically(1)：direction传正数，检测是否能向下滚动，如果ListView或者RecyclerView滚动到最后一个
     * item且完全显示，则不能再向下滚动
     *
     * @return
     */
    private boolean canScrollUp() {
        if (mScrollView != null) {
            return mScrollView.canScrollVertically(-1);
        }
        return false;
    }
}
