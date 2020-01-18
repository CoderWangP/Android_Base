package com.wp.android_base.base.http.interceptor

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wp.android_base.base.http.ApiException
import com.wp.android_base.base.http.CodeConstant
import com.wp.android_base.base.http.HttpResult
import com.wp.android_base.base.utils.log.Logger
import com.wp.android_base.model.GetWidBody
import com.wp.android_base.model.WidData
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
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
class XWIDInterceptorNew :BaseResponseBodyInterceptor(){

    private val TAG = "XWIDInterceptorNew"

    override fun intercept(chain: Interceptor.Chain, response: Response,url: String, json: String): Response {
        if(!TextUtils.isEmpty(json)){
            val httpResult = Gson().fromJson<HttpResult<*>>(json,HttpResult::class.java)
            val code = httpResult.code
            when(code){
                4,201 ->{
                    //需要xwid
                    throw ApiException.ResponseThrowable(Throwable("Missing wallet id"),code)
                }
                211 ->{
                    //需要初始化
                    throw ApiException.ResponseThrowable(Throwable("Wallet is not initialized"),code)
                }
            }
        }
        return response
    }
}