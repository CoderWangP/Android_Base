package com.wp.android_base.base.http.interceptor

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
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
class XWIDInterceptor :BaseResponseBodyInterceptor(){

    private val TAG = "XWIDInterceptor"

    private val base_url = "http://119.23.10.138:1880/"

    private var mXWid = "5e201effaa5294917eb13e07"

    override fun intercept(chain: Interceptor.Chain, response: Response,url: String, json: String): Response {
        if(!TextUtils.isEmpty(json)){
            val httpResult = Gson().fromJson<HttpResult<*>>(json,HttpResult::class.java)
            val code = httpResult.code
            when(code){
                4,201 ->{
                    //需要xwid
                    requestXWID(chain)
                    val newRequest = chain.request().newBuilder()
                            .removeHeader("X-WID")
                            .addHeader("X-WID", mXWid).build()
                    val newResponse = chain.proceed(newRequest)
                    val newJson = getResponseJson(newResponse)
                    if(!TextUtils.isEmpty(newJson)){
                        val newHttpResult = Gson().fromJson<HttpResult<*>>(newJson,HttpResult::class.java)
                        val newCode = newHttpResult.code
                        if(newCode == 211){
                            //需要初始化
                            initWallet(chain)
                            val newRequest1 = chain.request().newBuilder()
                                    .removeHeader("X-WID")
                                    .addHeader("X-WID", mXWid).build()
                            return chain.proceed(newRequest1)
                        }
                    }
                    return newResponse
                }
                211 ->{
                    //需要初始化
                    initWallet(chain)
                    val newRequest = chain.request().newBuilder()
                            .removeHeader("X-WID")
                            .addHeader("X-WID", mXWid).build()
                    return chain.proceed(newRequest)
                }
            }
        }
        return response
    }

    /**
     * 请求Wallet id
     * @param chain
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun requestXWID(chain: Interceptor.Chain) {
        val key = "xpub6DK7ng2Nj7Mg3aktsbx56vsPoMe4sxxe1aMxCF2CaL1R3E5Vsyxfc8j8hM3bJ3i2Dj8Ym6nAJ4UAp8oEZBaEfrsEesUPZTuhzXCSQNsxs7y"
        if (TextUtils.isEmpty(key)) {
            return
        }
        val url = base_url + "res/wallets/wid"
        val keyJson = Gson().toJson(GetWidBody(key))
        val requestBody = RequestBody.create(MediaType.parse("application/json"), keyJson)
        val builder = chain.request().newBuilder()
                .post(requestBody)
                .url(url)
        val response = chain.proceed(builder.build())
        val responseBody = response.body()
        if (responseBody != null) {
            val jsonString = responseBody.string()
            val widData = Gson().fromJson<WidData>(jsonString, WidData::class.java) as WidData?
            if (widData != null && widData.code == CodeConstant.CODE_OK) {
                val wid = widData.data
                if (wid != null && !TextUtils.isEmpty(wid.w_id)) {
                    val xwid = wid.w_id
                    mXWid = xwid
                }
            }
            Logger.e(TAG, "code=" + response.code(), "url=$url", "json = $jsonString")
        }
    }

    /**
     * 初始化Wallet
     * @param chain
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun initWallet(chain: Interceptor.Chain) {
        val url = base_url + "res/wallets/init"
        val initJson = Gson().toJson(getInitWalletBody())
        val requestBody = RequestBody.create(MediaType.parse("application/json"), initJson)
        val builder = chain.request().newBuilder()
                .removeHeader("X-WID")
                .addHeader("X-WID", mXWid)
                .post(requestBody)
                .url(url)
        val response = chain.proceed(builder.build())
        val responseBody = response.body()
        if (responseBody != null) {
            val jsonString = responseBody.string()
            val widData = Gson().fromJson<WidData>(jsonString, WidData::class.java)
            if (widData != null && widData.code == CodeConstant.CODE_OK) {
                val wid = widData.data
                if (wid != null && !TextUtils.isEmpty(wid.w_id)) {
                    mXWid = wid.w_id
                }
            }
            Logger.e(TAG, "code=" + response.code(), "url=$url", "json = $jsonString")
        }
    }

    private fun getResponseJson(response: Response):String?{
        val responseBody = response.body()
        if(responseBody != null){
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
                return buffer.clone().readString(charset)
            }
        }
        return null
    }


    //bch : xpub6CFj9JqD93BdaK8Fja3ZSnsx4BRq9EWZRqDJKjRwJXMNGWUdEx876AE1faSAUEDL7iA3HvV9moxehyK9k1TLfggcD1QDibz4G4BBYqXp45c
    private fun getInitWalletBody(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("bch" + "_xpub", "xpub6CFj9JqD93BdaK8Fja3ZSnsx4BRq9EWZRqDJKjRwJXMNGWUdEx876AE1faSAUEDL7iA3HvV9moxehyK9k1TLfggcD1QDibz4G4BBYqXp45c")
        return jsonObject
    }
}