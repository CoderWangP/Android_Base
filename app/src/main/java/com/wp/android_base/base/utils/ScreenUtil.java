package com.wp.android_base.base.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;

/**
 * Created by wangpeng on 2017/11/23.
 *
 * 屏幕工具，单位转换
 */

public class ScreenUtil {

    /**
     * 屏宽
     * @return
     */
    public static int getScreenWidth(){
        DisplayMetrics dm = AppModule.provideResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 屏高
     * @return
     */
    public static int getScreenHeight(){
        DisplayMetrics dm = AppModule.provideResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 状态栏高
     * @return
     */
    public static int getStatusBarHeight(){
        int height = 0;
        int statusBarId = AppModule.provideResources().getIdentifier("status_bar_height","dimen","android");
        if(statusBarId > 0){
            height = AppModule.provideResources().getDimensionPixelSize(statusBarId);
        }
        return height;
    }

    /**
     * ActionBar高度
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context) {

        TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[] {
                android.R.attr.actionBarSize
        });

        int actionBarHeight = (int) actionbarSizeTypedArray.getDimension(0, 0);
        actionbarSizeTypedArray.recycle();
        return actionBarHeight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        final float scale = AppModule.provideResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        final float scale = AppModule.provideResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 像素转sp
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = AppModule.provideResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转像素
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = AppModule.provideResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * sp转dp
     * @param spValue
     * @return
     */
    public static int sp2dp(float spValue){
        return px2dp(sp2px(spValue));
    }
}
