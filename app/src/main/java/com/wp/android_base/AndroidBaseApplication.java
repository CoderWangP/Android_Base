package com.wp.android_base;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.wp.android_base.base.http.ApiException;
import com.wp.android_base.base.http.HttpRequestManager;
import com.wp.android_base.api.gituser.GitUserApi;
import com.wp.android_base.base.http.HttpResult;
import com.wp.android_base.model.gituser.GitUser;
import com.wp.android_base.base.utils.AppModule;

import java.util.List;

/**
 * Created by wangpeng on 2018/6/21.
 *
 */

public class AndroidBaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary 内存泄漏检测
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        //注意，在AndroidManifest.xml文件中，注册该Application
        Stetho.initializeWithDefaults(this);
        AppModule.init(this);
        textHttpApi();
    }

    private void textHttpApi() {
        HttpRequestManager.createApi(GitUserApi.class).testNullApiImpl("octocat")
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult>(this) {
                    @Override
                    protected void onSuccess(HttpResult httpResult) {

                    }

                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {

                    }
                });

        HttpRequestManager.createApi(GitUserApi.class).getGitUser("octocat")
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult<List<GitUser>>>(this) {
                    @Override
                    protected void onSuccess(HttpResult<List<GitUser>> listHttpResult) {

                    }

                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {

                    }
                });
    }

}
