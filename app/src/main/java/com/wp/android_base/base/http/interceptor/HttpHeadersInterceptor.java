package com.wp.android_base.base.http.interceptor;

import android.support.annotation.NonNull;


import com.wp.android_base.base.utils.PackageUtil;
import com.wp.android_base.base.utils.SystemUtil;
import com.wp.android_base.base.utils.language.LanguageUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wp on 2018/6/28.
 * 头部Interceptor
 */

public class HttpHeadersInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        //添加header数据
        Request request = chain.request().newBuilder()
                .addHeader("Accept-Language", LanguageUtil.getCurrentAppLangByStr())
                .addHeader("X-Platform", "Android")
                .removeHeader("User-Agent")
                .addHeader("User-Agent",getUserAgent())
                .build();
        return chain.proceed(request);
    }


    private static String getUserAgent() {
        String userAgent = "";
        String versionName = PackageUtil.getVersionName();
        String versionCode = PackageUtil.getVersionCode();
        String appName = PackageUtil.getAppName();
        String packageName = PackageUtil.getPackageName();
        String systemVersion = SystemUtil.getSystemVersion();
        userAgent = appName + "/" + versionName + "(" + packageName +";build:" + versionCode + ";Android" +  systemVersion + ")" +  "okhttp3/3.10.0";
        return userAgent;
    }
}
