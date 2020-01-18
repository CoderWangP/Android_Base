package com.wp.android_base.base.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 *
 * Created by wp on 2020/1/16.
 *
 * Description:
 *
 */
abstract class BaseResponseBodyInterceptor :Interceptor{

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().toString()
        val response = chain.proceed(request)
        val responseBody = response.body()
        if (responseBody != null) {
            val contentLength = responseBody.contentLength()
            val headers = response.headers()
            val source = responseBody.source()
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            var buffer = source.buffer()

            if ("gzip".equals(headers.get("Content-Encoding"), ignoreCase = true)) {
                var gzippedResponseBody: GzipSource? = null
                try {
                    gzippedResponseBody = GzipSource(buffer.clone())
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                } finally {
                    if (gzippedResponseBody != null) {
                        gzippedResponseBody.close()
                    }
                }
            }
            var charset: Charset? = StandardCharsets.UTF_8
            val contentType = responseBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(StandardCharsets.UTF_8)
            }
            if (charset != null && contentLength != 0L) {
                return intercept(chain, response, url, buffer.clone().readString(charset))
            }
        }
        return response
    }

    abstract fun intercept(chain: Interceptor.Chain,response: Response,url:String,json:String):Response
}