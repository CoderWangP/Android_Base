package com.wp.android_base.base.http;

import android.app.Application;
import android.content.Context;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.wp.android_base.LoginActivity;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.BaseFragment;
import com.wp.android_base.base.utils.AppModule;
import com.wp.android_base.base.utils.CheckLifecycleUtil;
import com.wp.android_base.base.utils.log.Logger;
import com.wp.android_base.base.widget.dialog.base.BaseDialog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wangpeng on 2018/6/29.
 */

public abstract class BaseHttpResponseObserver<T> implements Observer<T> {

    private static final String TAG = "BaseHttpResponseObserver";

    private static final boolean IS_DEBUG = false;

    private LifecycleProvider mLifecycleProvider;

    private Application mApplication;

    BaseHttpResponseObserver(LifecycleProvider lifecycleProvider) {
        this.mLifecycleProvider = lifecycleProvider;
    }

    BaseHttpResponseObserver(Application application) {
        this.mApplication = application;
    }

    @Override
    public void onSubscribe(Disposable d) {
        Logger.d(TAG, "onSubscribe()");
    }

    @Override
    public void onNext(T t) {
        if (mApplication != null) {
            onSuccess(t);
        } else if (mLifecycleProvider != null) {
            onLifecycleProviderAlive(t);
        }
    }

    protected abstract void onSuccess(T t);

    @Override
    public void onError(Throwable e) {
        if (mApplication != null) {
            ApiException.ResponseThrowable responseThrowable = ApiException.handleException(e);
            Logger.d(TAG, responseThrowable.getMessage());
            onError(responseThrowable);
        } else if (mLifecycleProvider != null) {
            onLifecycleProviderAlive(e);
        }
    }

    private void onLifecycleProviderAlive(T t) {
        if (!isLifecycleProviderAlive()) {
            Logger.e("LifecycleProvider destroy");
            return;
        }
        onSuccess(t);
    }


    private void onLifecycleProviderAlive(Throwable e) {
        if (!isLifecycleProviderAlive()) {
            Logger.e("LifecycleProvider destroy");
            return;
        }
        ApiException.ResponseThrowable responseThrowable = ApiException.handleException(e);
        Logger.e(TAG, responseThrowable.code,responseThrowable.getMessage());
        if(responseThrowable.code == ApiException.UNAUTHORIZED){
            onAuthError(responseThrowable);
        }else{
            onError(responseThrowable);
        }
    }

    @Override
    public void onComplete() {
    }


    protected abstract void onError(ApiException.ResponseThrowable responseThrowable);

    private void onAuthError(ApiException.ResponseThrowable responseThrowable) {
        if (responseThrowable.getCode() == ApiException.UNAUTHORIZED) {
            forward2Login();
        }
    }

    private boolean isLifecycleProviderAlive() {
        if (mLifecycleProvider instanceof RxAppCompatActivity) {
            RxAppCompatActivity baseActivity = (RxAppCompatActivity) mLifecycleProvider;
            return CheckLifecycleUtil.isAlive(baseActivity);
        }
        if (mLifecycleProvider instanceof RxFragment) {
            RxFragment baseFragment = (RxFragment) mLifecycleProvider;
            return CheckLifecycleUtil.isAlive(baseFragment);
        }
        return false;
    }


    private void forward2Login() {
        if (mLifecycleProvider == null && mApplication == null) {
            return;
        }
        Context context = null;
        if(mLifecycleProvider != null && mApplication == null){
            if (mLifecycleProvider instanceof BaseFragment) {
                context = ((BaseFragment) mLifecycleProvider).getActivity();
            }
            if (mLifecycleProvider instanceof BaseActivity) {
                context = (Context) mLifecycleProvider;
            }
            if (mLifecycleProvider instanceof BaseDialog) {
                context = ((BaseDialog) mLifecycleProvider).getActivity();
            }
        }else if(mLifecycleProvider == null){
            context = mApplication;
        }


        if(context == null){
            context = AppModule.provideApplication();
        }

        if (context == null) {
            if (IS_DEBUG) {
                throw new IllegalArgumentException("Context can not be null");
            } else {
                return;
            }
        }
        LoginActivity.forward2Login(context);
    }
}
