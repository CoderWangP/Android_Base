package com.wp.android_base.test.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.log.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * Created by wp on 2019/5/21.
 * <p>
 * Description:
 */

public class CustomViewStateActivity extends BaseActivity{

    private static final String TAG = "CustomViewStateActivity";

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_custom_view_state;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e(TAG,"onCreate");
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
