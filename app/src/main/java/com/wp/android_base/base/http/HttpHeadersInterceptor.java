package com.wp.android_base.base.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangpeng on 2018/6/28.
 * 头部Interceptor
 */

public class HttpHeadersInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
/*        builder.addHeader("platform", "Android");*/
        return chain.proceed(builder.build());
    }
}
