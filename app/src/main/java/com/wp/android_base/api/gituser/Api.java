package com.wp.android_base.api.gituser;

import com.wp.android_base.base.http.HttpResult;
import com.wp.android_base.model.Balance;
import com.wp.android_base.model.GetWidBody;
import com.wp.android_base.model.TokenItem;
import com.wp.android_base.model.WidData;
import com.wp.android_base.model.gituser.GitUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by wangpeng on 2018/6/22.
 */

public interface Api {

    @GET("users/{user}/repos")
    Observable<HttpResult<List<GitUser>>> getGitUser(@Path("user") String user);

    @GET("users/{user}/repos")
    Observable<HttpResult<GitUser>> test(@Path("user") String user);

    @GET("users/{user}/repos")
    Observable<HttpResult> testNullApiImpl(@Path("user") String user);


    @GET("/repos/{owner}/{repo}/contributors")
    Observable<HttpResult> testLogInterceptorImpl(@Path("owner") String owner,@Path("repo")String repo);


    /**
     * 有余额的币种
     * @return
     */
    @GET
    Observable<HttpResult<List<TokenItem>>> richTokens(@Header("X-WID") String xwid, @Url String url);


    /**
     * 获取余额 单个币种
     *
     * @return
     */
    @GET
    Observable<HttpResult<Balance>> getBalance(@Header("X-WID") String xwid, @Url String url);


    /**
     * 获取wid
     * @return
     */
    @POST
    Observable<HttpResult<WidData.Wid>> getWid(@Body GetWidBody getWidBody,@Url String url);


    @POST
    Call<HttpResult<WidData.Wid>> getWidSync(@Body GetWidBody getWidBody,@Url String url);


}
