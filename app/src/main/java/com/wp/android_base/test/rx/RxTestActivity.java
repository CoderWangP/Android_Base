package com.wp.android_base.test.rx;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.log.Logger;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by wp on 2019/4/18.
 * <p>
 * Description:
 */

public class RxTestActivity extends BaseActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_rxtest;
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();
        takeUntil();
        takeWhile();
    }

    /**
     * takeUntil接受Predicate,Observable，test方法返回false时，被观察者事件会执行，
     * 返回true时，被观察者事件被打断，取消订阅,而且会执行本次为true的事件
     */
    private void takeUntil(){
        Observable.just(1, 2, 3)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Logger.e("takeUntil", "doOnDispose");
                    }
                })
                .takeUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer == 2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Logger.e("takeUntil", integer);
                    }
                });
    }

    /**
     * 与takeUntil(接收Observable,Predicate)不同，takeWhile只接受Predicate,
     * 而且test方法返回true时，才执行被观察者事件，且被观察者事件被打断，取消订阅
     */
    private void takeWhile(){
        Observable.just(1, 2, 3)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Logger.e("takeWhile", "doOnDispose");
                    }
                })
                .takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer != 2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Logger.e("takeWhile", integer);
                    }
                });
    }
}
