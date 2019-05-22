package com.wp.android_base.test.check.lifeccycle;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.log.Logger;
import com.wp.android_base.model.gituser.GitUser;
import com.wp.android_base.test.base.dialog.MyDialogFragment;
import com.wp.android_base.test.view.CustomViewStateActivity;

import java.util.HashSet;

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

    private static final String TAG = "LifecycleTestActivity";

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void forward2Another(View v){
        startActivity(new Intent(this, CustomViewStateActivity.class));
        getValue();
//        finish();
        HashSet<GitUser> hashSet = new HashSet();
        hashSet.add(new GitUser());
    }

    public void showDialog(View view){
/*        MyDialogFragment myDialogFragment = new MyDialogFragment();
        myDialogFragment.show(getSupportFragmentManager());*/

        new AlertDialog.Builder(this)
                .setTitle("这是一个弹窗")
                .setIcon(R.drawable.glide_test_dog).show();
    }

    public int getValue(){
        try {
           throw new RuntimeException("抛出异常");
        }catch (Exception e){
            Logger.e(TAG,"catch");
        }finally {
            Logger.e(TAG,"finally");
        }
        Logger.e(TAG,"return");
        return -1;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_check_lifecycler;
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
