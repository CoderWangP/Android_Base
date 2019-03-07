package com.wp.android_base.base.image.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.wp.android_base.R;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by wangpeng on 2018/7/20.
 * Glide的全局配置，注意配置完成之后，需要build project才能生成GlideApp
 */

@GlideModule
public class MyAppGlideModule extends AppGlideModule{

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.shape_default_image_place_holder)
                .error(R.drawable.shape_default_image_place_holder)
                /*配置图片的DecodeFormat,默认为ARGB_565*/
                .format(DecodeFormat.PREFER_ARGB_8888);
        builder.setDefaultRequestOptions(requestOptions);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //配置glide的网络加载的自定义Client
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addNetworkInterceptor(new StethoInterceptor());
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(builder.build());
        registry.replace(GlideUrl.class, InputStream.class,factory);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        //不使用清单配置的方式,减少初始化时间
        return false;
    }
}
