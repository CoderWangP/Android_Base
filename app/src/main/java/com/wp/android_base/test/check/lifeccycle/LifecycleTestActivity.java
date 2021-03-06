package com.wp.android_base.test.check.lifeccycle;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.log.Logger;
import com.wp.android_base.model.gituser.GitUser;
import com.wp.android_base.test.base.dialog.MyDialogFragment;
import com.wp.android_base.test.view.CustomViewStateActivity;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

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
        Logger.e(TAG,"getValue=" + getValue());
    }

    public void showDialog(View view){
        //建造者模式
        new AlertDialog.Builder(this)
                .setTitle("这是一个弹窗")
                .setIcon(R.drawable.glide_test_dog).show();
    }

    public int getValue(){
        int i=-1;
        try {
           throw new RuntimeException("抛出异常");
        }catch (Exception e){
            Logger.e(TAG,"catch");
            i = 10;
            throw new RuntimeException("catch e");
        }finally {
            Logger.e(TAG,"finally");
            i = -3;
            return i;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_check_lifecycler;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e(TAG,"onCreate");
        GitUser gitUser = new GitUser();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.e(TAG,"onRestart");
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
