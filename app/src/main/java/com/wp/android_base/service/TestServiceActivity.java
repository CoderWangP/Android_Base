package com.wp.android_base.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.log.Logger;

/**
 * Created by wp on 2019/5/20.
 * <p>
 * Description:1.以bindService已经启动的Service,再次调用bindService，不会重新启动，
 *               但是再次以startService启动的话，不会经历onCreate,但是会调用onStartCommand,
 *               关闭该启动组件时，会调用onUnbind,但是不会调用onDestroy，不会销毁
 *
 */

public class TestServiceActivity extends BaseActivity{

    private static final String TAG = "TestServiceActivity";

    private boolean mIsBound;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.e(TAG,"onServiceConnected");
            mIsBound = true;
            MyService.MyBinder binder = (MyService.MyBinder) service;
            MyService myService = binder.getService();
            myService.contactMethod();
        }

        /**
         * 当服务异常终止时会调用。注意，解除绑定服务时不会调用
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.e(TAG,"onServiceDisconnected","ComponentName=" + name.getClassName());
            mIsBound = false;
        }
    };

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_service_test;
    }

    public void startService(View view) {
        Intent intent = new Intent(this,MyService.class);
        startService(intent);
    }

    /**
     * 绑定服务
     * @param view
     */
    public void bindService(View view) {
        Intent intent = new Intent(this,MyService.class);
        this.bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unBindService(View view) {
        this.unbindService(mServiceConnection);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mServiceConnection != null && mIsBound){
            this.unbindService(mServiceConnection);
        }
    }
}
