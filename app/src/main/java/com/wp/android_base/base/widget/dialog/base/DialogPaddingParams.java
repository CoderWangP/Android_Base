package com.wp.android_base.base.widget.dialog.base;

/**
 * Created by wangpeng on 2018/7/24.
 */

public class DialogPaddingParams {

    public int left;
    public int top;
    public int right;
    public int bottom;

    public DialogPaddingParams() {
    }

    public DialogPaddingParams(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }
}
