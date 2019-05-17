package com.wp.android_base.base.widget.recyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wp.android_base.base.utils.ScreenUtil;

/**
 * Created by wangpeng on 2018/9/18.
 * <p>
 * Description:LinearLayoutManager的间隔
 */

public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDividerDrawable;
    public int mDividerSize;
    //第一条item前是否加ItemDecoration
    public boolean mIsStartAddDecoration;
    //最后一条item后是否加ItemDecoration
    public boolean mIsEndAddDecoration;

    public LinearItemDecoration(Drawable dividerDrawable, int dividerSize) {
        this(dividerDrawable, dividerSize, false, false);
    }

    public LinearItemDecoration(Drawable dividerDrawable, int dividerSize, boolean isStartAddDecoration, boolean isEndAddDecoration) {
        this.mDividerDrawable = dividerDrawable;
        this.mDividerSize = dividerSize;
        this.mIsStartAddDecoration = isStartAddDecoration;
        this.mIsEndAddDecoration = isEndAddDecoration;
    }

    public LinearItemDecoration(String color, int dividerSize) {
        this(Color.parseColor(color), dividerSize);
    }

    public LinearItemDecoration(int color, int dividerSize) {
        this(color, dividerSize, false, false);
    }

    public LinearItemDecoration(int color, int dividerSize, boolean isStartAddDecoration, boolean isEndAddDecoration) {
        this.mDividerDrawable = new ColorDrawable(color);
        this.mDividerSize = dividerSize;
        this.mIsStartAddDecoration = isStartAddDecoration;
        this.mIsEndAddDecoration = isEndAddDecoration;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDividerDrawable == null) {
            return;
        }
        int childCount = parent.getChildCount();
        if (childCount <= 0) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        int needDecorationCount = childCount - 1;
        if (mIsEndAddDecoration) {
            needDecorationCount = childCount;
        }
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int left;
            int right;
            int top;
            int bottom;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                top = parent.getPaddingTop();
                bottom = parent.getHeight() - parent.getPaddingBottom();
                //计算每个item的divider的宽高及位置
                for (int i = 0; i < needDecorationCount; i++) {
                    View childView = parent.getChildAt(i);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
                    if (i == 0 && mIsStartAddDecoration) {
                        int startLeft = parent.getPaddingLeft();
                        int startRight = startLeft + mDividerSize;
                        mDividerDrawable.setBounds(startLeft, top, startRight, bottom);
                        mDividerDrawable.draw(c);
                    }
                    left = childView.getRight() + params.rightMargin;
                    right = left + mDividerSize;
                    mDividerDrawable.setBounds(left, top, right, bottom);
                    mDividerDrawable.draw(c);
                }
            } else {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
                for (int i = 0; i < needDecorationCount; i++) {
                    View childView = parent.getChildAt(i);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
                    if (i == 0 && mIsStartAddDecoration) {
                        int startTop = parent.getPaddingTop();
                        int startBottom = startTop + mDividerSize;
                        mDividerDrawable.setBounds(left, startTop, right, startBottom);
                        mDividerDrawable.draw(c);
                    }
                    top = childView.getBottom() + params.bottomMargin;
                    bottom = top + mDividerSize;
                    mDividerDrawable.setBounds(left, top, right, bottom);
                    mDividerDrawable.draw(c);
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDividerDrawable == null) {
            return;
        }
        int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == RecyclerView.NO_POSITION) {
            return;
        }
        int itemCount = state.getItemCount();
        if (itemCount <= 0) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                if (itemPosition == 0 && mIsStartAddDecoration) {
                    outRect.set(mDividerSize, 0, mDividerSize, 0);
                } else if (itemPosition == itemCount - 1) {
                    if (mIsEndAddDecoration) {
                        outRect.set(0, 0, mDividerSize, 0);
                    } else {
                        outRect.set(0, 0, 0, 0);
                    }
                } else {
                    outRect.set(0, 0, mDividerSize, 0);
                }
            } else {

                if (itemPosition == 0 && mIsStartAddDecoration) {
                    outRect.set(0, mDividerSize, 0, mDividerSize);
                } else if (itemPosition == itemCount - 1) {
                    if (mIsEndAddDecoration) {
                        outRect.set(0, 0, 0, mDividerSize);
                    } else {
                        outRect.set(0, 0, 0, 0);
                    }
                } else {
                    outRect.set(0, 0, 0, mDividerSize);
                }
            }
        }
    }
}
