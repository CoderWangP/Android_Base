package com.wp.android_base.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wp.android_base.base.utils.log.Logger;


/**
 * Created by wp on 2019/5/20.
 * <p>
 * Description:
 */

public class MyService extends Service{

    private static final String TAG = "MyService";


    public class MyBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }
    }

    private MyBinder mMyBinder = new MyBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.e(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.e(TAG,"onBind");
        return mMyBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.e(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    /**
     * 通过bindService方法，让调用者与Service通信的方法
     */
    public void contactMethod(){
        Logger.e(TAG,"调用者与Service交互的方法被调用");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.e(TAG,"onDestroy");
    }
}
