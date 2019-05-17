package com.wp.android_base.base;


import com.wp.android_base.base.utils.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wangpeng on 2019/1/9.
 * <p>
 * Description:
 */

public abstract class SimpleObserver<T> implements Observer<T>{


    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        ToastUtil.show(e.getMessage());
    }
}
