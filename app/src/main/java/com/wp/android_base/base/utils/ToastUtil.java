package com.wp.android_base.base.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by wangpeng on 2018/6/25.
 * Toast工具类
 */

public class ToastUtil {

    private static final int sDuration = Toast.LENGTH_SHORT;
    private static Toast sToast;

    /**
     * Toast显示
     * @param messageResId
     */
    public static void show(@StringRes int messageResId) {
        if (messageResId == 0){
            return;
        }
        Context context = AppModule.provideApplication();
        if(context == null){
            return;
        }
        if(sToast == null){
            sToast = Toast.makeText(context,messageResId,sDuration);
        }else{
            sToast.setText(messageResId);
        }
        sToast.show();
    }

    /**
     *
     * @param message
     */
    public static void show(String message){
        if (TextUtils.isEmpty(message)){
            return;
        }
        Context context = AppModule.provideApplication();
        if(context == null){
            return;
        }
        if(sToast == null){
            sToast = Toast.makeText(context,message,sDuration);
        }else{
            sToast.setText(message);
        }
        sToast.show();
    }

    /**
     * Toast显示，改变Gravity，显示在屏幕中间
     * @param message
     */
    public static void showInCenter(String message) {
        if (TextUtils.isEmpty(message)) return;
        Context context = AppModule.provideApplication();
        if(context == null){
            return;
        }
        if(sToast == null){
            sToast = Toast.makeText(context, message, sDuration);
        }else{
            sToast.setText(message);
        }
        sToast.setGravity(Gravity.CENTER,0,0);
        sToast.show();
    }

    /**
     *
     * @param messageResId
     */
    public static void showInCenter(@StringRes int messageResId) {
        if (messageResId == 0){
            return;
        }
        Context context = AppModule.provideApplication();
        if(context == null){
            return;
        }
        if(sToast == null){
            sToast = Toast.makeText(context, messageResId, sDuration);
        }else{
            sToast.setText(messageResId);
        }
        sToast.setGravity(Gravity.CENTER,0,0);
        sToast.show();
    }
}
