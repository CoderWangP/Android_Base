package com.wp.android_base.api.gituser;

import com.wp.android_base.base.http.HttpResult;
import com.wp.android_base.model.gituser.GitUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by wangpeng on 2018/6/22.
 */

public interface GitUserApi {

    @GET("users/{user}/repos")
    Observable<HttpResult<List<GitUser>>> getGitUser(@Path("user") String user);

    @GET("users/{user}/repos")
    Observable<HttpResult<GitUser>> test(@Path("user") String user);

    @GET("users/{user}/repos")
    Observable<HttpResult> testNullApiImpl(@Path("user") String user);


    @GET("/repos/{owner}/{repo}/contributors")
    Observable<HttpResult> testLogInterceptorImpl(@Path("owner") String owner,@Path("repo")String repo);
}
