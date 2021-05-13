package com.wp.android_base.base.http;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.GsonBuilder;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.wp.android_base.BuildConfig;
import com.wp.android_base.Constants;
import com.wp.android_base.api.gituser.Api;
import com.wp.android_base.base.http.interceptor.HttpHeadersInterceptor;
import com.wp.android_base.base.utils.AppModule;
import com.wp.android_base.base.utils.Sp;
import com.wp.android_base.base.utils.log.Logger;
import com.wp.android_base.model.GetWidBody;
import com.wp.android_base.model.WidData;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by wangpeng on 2018/6/22.
 *
 * 网络请求管理类
 */
public class HttpRequestManager implements CodeConstant {

    private static final String TAG = "HttpRequestManager";

    private static final int DEFAULT_TIMEOUT = 30;
    private static Retrofit sRetrofit;

    private static final String sBaseUrl = "https://api.github.com/";

//    private static final String sBaseUrl = "https://api.coinex.com/v1/";

    private static HashMap<Class, WeakReference> sApiCache = new LinkedHashMap<>();

    private HttpRequestManager() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    @SuppressWarnings("unchecked")
    public static <T> T createApi(Class<T> tClass) {
        T t;
        WeakReference<T> weakReference = sApiCache.get(tClass);
        if (weakReference != null) {
            t = weakReference.get();
            if (t != null) {
                return t;
            } else {
                //弱引用在，但是没有值，直接将该键保存的弱引用删除
                sApiCache.remove(tClass);
            }
        }
        if (sRetrofit == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                    //添加头部
                    .addInterceptor(new HttpHeadersInterceptor())
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

            //debug下再添加一些调试的工具（日志，网络抓包）
            if (BuildConfig.DEBUG) {
                httpClientBuilder
                        .addInterceptor(new HttpLoggingInterceptor(message -> {
                            if (message.startsWith("{") || message.startsWith("[")) {
                                Logger.json(TAG, message);
                            } else {
                                Logger.d(TAG, message);
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                        //stetho 网络抓包
                        .addNetworkInterceptor(new StethoInterceptor());
            }
            sRetrofit = new Retrofit.Builder()
                    .client(httpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(sBaseUrl)
                    .build();
        }
        t = sRetrofit.create(tClass);
        WeakReference<T> cacheT = new WeakReference<>(t);
        sApiCache.put(tClass, cacheT);
        return t;
    }


    public static class DefaultTransformer<T> implements ObservableTransformer<T, T> {

        private LifecycleProvider mLifecycleProvider;
        private Application mApplication;

        private DefaultTransformer(LifecycleProvider lifecycleProvider) {
            this.mLifecycleProvider = lifecycleProvider;
        }

        /**
         * 空LifecycleProvider，生命周期Application
         */
        private DefaultTransformer(Application application) {
            this.mApplication = application;
        }

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            if (mLifecycleProvider == null && mApplication != null) {
                return upstream
                        .doOnDispose(() -> Logger.e("doOnDispose", "Unsubscribing subscription"))
                        .retryWhen(new MyRetryWhenHandler())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            } else {
                return upstream
                        .doOnDispose(() -> Logger.e("doOnDispose", "Unsubscribing subscription"))
                        .retryWhen(new MyRetryWhenHandler())
                        .compose(HttpRequestManager.bind2Lifecycle(mLifecycleProvider))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }
    }


    @SuppressWarnings("unchecked")
    @NonNull
    private static <T> LifecycleTransformer<T> bind2Lifecycle(LifecycleProvider lifecycleProvider) {

        if (lifecycleProvider == null) {
            throw new IllegalArgumentException("LifecycleProvider can not be null");
        }
        if (lifecycleProvider instanceof RxAppCompatActivity) {
            return lifecycleProvider.bindUntilEvent(ActivityEvent.DESTROY);
        }
        if (lifecycleProvider instanceof RxFragment) {
            return lifecycleProvider.bindUntilEvent(FragmentEvent.DESTROY);
        }
        throw new IllegalArgumentException("LifecycleProvider must be ? extends (RxAppCompatActivity,RxFragment)");
    }


    /**
     * Retrofit的api方法返回Observable的默认统一操作，包括利用RxLifecycle绑定生命周期，指定事件产生的线程、事件消费线程
     * @param lifecycleProvider
     * @param <T>
     * @return
     */
    @NonNull
    public static <T> DefaultTransformer<T> createDefaultTransformer(LifecycleProvider lifecycleProvider) {
        return new DefaultTransformer<>(lifecycleProvider);
    }

    /**
     * 在Application会用到，一般很少用到这个，与{@link #createDefaultTransformer(LifecycleProvider)}方法主要区别是
     * 本方法没有利用到RxLifecycle，只指定了事件产生的线程、事件消费线程
     *
     * Retrofit的api方法返回Observable的默认统一操作，包括利用RxLifecycle绑定生命周期，指定事件产生的线程、事件消费线程
     *
     * @param application
     * @param <T>
     * @return
     */
    @NonNull
    public static <T> DefaultTransformer<T> createDefaultTransformer(Application application) {
        return new DefaultTransformer<>(application);
    }

    public static abstract class SimpleObserver<T> extends BaseHttpResponseObserver<T> {

        protected SimpleObserver(LifecycleProvider lifecycleProvider) {
            super(lifecycleProvider);
        }

        protected SimpleObserver(Application application) {
            super(application);
        }
    }


    static class MyRetryWhenHandler implements Function<Observable<Throwable>,ObservableSource<?>> {
        @Override
        public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {

            return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                @Override
                public ObservableSource<?> apply(Throwable throwable) throws Exception {
                    if(throwable instanceof ApiException.ResponseThrowable){
                        ApiException.ResponseThrowable apiException = (ApiException.ResponseThrowable) throwable;
                        if(apiException.code == 4 || apiException.code == 201){
                            //缺少wid
                            String key = "xpub6DK7ng2Nj7Mg3aktsbx56vsPoMe4sxxe1aMxCF2CaL1R3E5Vsyxfc8j8hM3bJ3i2Dj8Ym6nAJ4UAp8oEZBaEfrsEesUPZTuhzXCSQNsxs7y";
                            Call<HttpResult<WidData.Wid>> call = createApi(Api.class).getWidSync(new GetWidBody(key),"http://119.23.10.138:1880/res/wallets/wid");
                            HttpResult<WidData.Wid> httpResult = call.execute().body();
                            if(httpResult != null){
                                if(httpResult.getCode() == 0){
                                    WidData.Wid wid = httpResult.getData();
                                    if(wid != null && !TextUtils.isEmpty(wid.getW_id())){
                                        Sp.from(AppModule.provideContext(), Constants.SP_CONFIG).writer().putString("wid",wid.getW_id()).apply();
                                        return Observable.just(wid);
                                    }
                                }
                            }
                        }
                    }
                    //其他情况，直接将error返回
                    return Observable.error(throwable);
                }
            });
        }
    }

}
