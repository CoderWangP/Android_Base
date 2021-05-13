package com.wp.android_base.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangpeng on 2018/10/20.
 *
 * SharedPreferences工具类
 */
public class Sp {

    private final static String ANDROID_BASE_SP = "androidBaseSp";

    private Context mContext;
    private String mSpName;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mSpEditor;

    private static HashMap<String, WeakReference<Sp>> sSpCache = new LinkedHashMap<>();

    private Sp(Context context, String spName) {
        mContext = context.getApplicationContext();
        if(mContext == null){
            mContext = context;
        }
        if (TextUtils.isEmpty(spName)) {
            mSpName = ANDROID_BASE_SP;
        } else {
            mSpName = spName;
        }
    }

    public static Sp from(Context context) {
        return from(context, null);
    }

    public static Sp from(Context context, String spName) {
        String cacheKey = spName != null ? spName : "default";
        WeakReference<Sp> cacheSpRef = sSpCache.get(cacheKey);
        Sp targetSp = null;
        if (cacheSpRef != null) {
            targetSp = cacheSpRef.get();
            if (targetSp == null) {
                sSpCache.remove(cacheKey);
            }
        }
        if (targetSp == null) {
            targetSp = new Sp(context, spName);
            cacheSpRef = new WeakReference<>(targetSp);
            sSpCache.put(cacheKey, cacheSpRef);
        }
        return targetSp;
    }

    public SharedPreferences read() {
        if (mSp == null) {
            mSp = mContext.getSharedPreferences(mSpName, Context.MODE_PRIVATE);
        }

        return mSp;
    }

    public SharedPreferences.Editor writer() {
        if (mSpEditor == null) {
            mSpEditor = read().edit();
        }
        return mSpEditor;
    }

    /**
     * 从SharePreferences存储中读json
     *
     * 使用时特别注意 ：默认事件产生的线程在io线程，而且Sp需要Context创建，如果Context是Activity，可能会内存泄漏，
     * 这时注意在用的地方，利用RxLifecycle关联到Context的生命周期，详细用法可见{@link com.wp.android_base.test.base.SpTestActivity},
     * {@link #writeJson(String,T)}也是一样
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> Observable<T> readJson(String key, Class<T> tClass) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                if (TextUtils.isEmpty(key)) {
                    e.onError(new Throwable("key can not be null"));
                }
                String json = read().getString(key, null);
                if (TextUtils.isEmpty(json)) {
                    e.onError(new Throwable("json is null"));
                } else {
                    T t = new Gson().fromJson(json, tClass);
                    e.onNext(t);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 向SharePreferences存储json
     *
     * 使用时特别注意 ：默认事件产生的线程在io线程，而且Sp需要Context创建，如果Context是Activity，可能会内存泄漏，
     * 这时注意在用的地方，利用RxLifecycle关联到Context的生命周期，详细用法可见{@link com.wp.android_base.test.base.SpTestActivity},
     * {@link #readJson(String,Class<T>)}也是一样
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> Observable<String> writeJson(String key, T t) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (TextUtils.isEmpty(key)) {
                    e.onError(new Throwable("key can not be null"));
                }
                if (t == null) {
                    e.onError(new Throwable("T can not be null"));
                }
                String json = new Gson().toJson(t);
                //成功提交到内存，不能确定是否成功存储到硬盘
                writer().putString(key, json).apply();
                e.onNext(json);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Sp读写异步时用的Observer，只是简化实现方法
     * @param <T>
     */
    public abstract static class SpObserver<T> implements Observer<T> {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onComplete() {

        }
    }
}
