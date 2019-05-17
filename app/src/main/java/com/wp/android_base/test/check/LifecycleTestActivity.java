package com.wp.android_base.test.check;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.log.Logger;

/**
 * Created by wangpeng on 2018/10/29.
 * <p>
 * Description:
 *
 * 1.Activity的方向根据重力感应自动变化，需要在Manifest的注册的Activity里添加
 *   android:screenOrientation="sensor"属性
 *
 * 2.sdk版本 > 13,后，要想activity横竖屏切换，不重新创建，需要在Manifest的注册的Activity里添加
 *   android:configChanges="keyboardHidden|orientation|screenSize"
 *   而sdk < 13,android:configChanges="keyboardHidden|orientation"
 *
 * 3.横竖屏切换生命周期
 *
 *   不配置configChanges，生命周期(从Activity创建开始)：
 *   onCreate -> onStart -> onResume -> onPause -> onSaveInstanceState -> onStop -> onDestory
 *   -> onCreate -> onStart -> onRestoreInstanceState -> onResume
 *
 *
 *   配置了configChanges，生命周期(从Activity创建开始)：
 *    onCreate -> onStart -> onResume -> onConfigurationChanged
 */

public class LifecycleTestActivity extends BaseActivity{

    public static final String TAG = "LifecycleTestActivity";

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_check_lifecycler;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e(TAG,"onCreate");
     /*   Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8000);
                    Logger.e(TAG,System.currentTimeMillis() / 1000 + "");
                    Logger.e(TAG,"AAA");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(12000);
                    Logger.e(TAG,System.currentTimeMillis() / 1000 + "");
                    Logger.e(TAG,"BBB");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.e(TAG,"onStart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Logger.e(TAG,"onRestoreInstanceState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.e(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.e(TAG,"onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Logger.e(TAG,"onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.e(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e(TAG,"onDestroy");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.e(TAG,"onConfigurationChanged");
    }
}
