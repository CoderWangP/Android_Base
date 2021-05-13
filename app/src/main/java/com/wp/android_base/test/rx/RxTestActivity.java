package com.wp.android_base.test.rx;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.SimpleObserver;
import com.wp.android_base.base.http.ApiException;
import com.wp.android_base.base.http.HttpRequestManager;
import com.wp.android_base.base.utils.ToastUtil;
import com.wp.android_base.base.utils.log.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wp on 2019/4/18.
 * <p>
 * Description:
 */

public class RxTestActivity extends BaseActivity {

    static final String TAG = "RxTestActivity";

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_rxtest;
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();
        takeUntil();
        takeWhile();
        retryWhen();
        create();
    }

    private void create() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                int i = 0;
                i++;
                emitter.onError(new Throwable("测试崩溃"));
                emitter.onNext(String.valueOf(i));
            }
        }).compose(HttpRequestManager.createDefaultTransformer(this))
        .subscribe(new HttpRequestManager.SimpleObserver<String>(this) {
            @Override
            protected void onSuccess(String s) {
                Logger.d(TAG,"s = " + s);
            }

            @Override
            protected void onError(ApiException.ResponseThrowable responseThrowable) {
                ToastUtil.show(responseThrowable.message);
            }
        });
    }

    private void retryWhen() {
        Observable
                .just(1,2,3,4,5,6,7,8,9,10)
                .flatMap(new Function<Integer, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Integer integer) throws Exception {
                        if(integer < 7){
                            //重试3次
                            return Observable.error(new Throwable("重试"));
                        }else{
                            return Observable.just(integer);
                        }
                    }
                })
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return Observable.timer(5, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        Logger.e("retryWhen","integer = " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    /**
     * takeUntil接受Predicate,Observable，test方法返回false时，被观察者事件会执行，
     * 返回true时，被观察者事件被打断，取消订阅,而且会执行本次为true的事件
     */
    private void takeUntil() {
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
    private void takeWhile() {
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
