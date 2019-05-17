package com.wp.android_base.test.base;

import android.view.View;
import android.widget.TextView;

import com.wp.android_base.LoginActivity;
import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.http.ApiException;
import com.wp.android_base.base.http.CodeConstant;
import com.wp.android_base.base.http.HttpRequestManager;
import com.wp.android_base.api.gituser.GitUserApi;
import com.wp.android_base.base.http.HttpResult;
import com.wp.android_base.api.gituser.GitUserApiImpl;
import com.wp.android_base.model.gituser.GitUser;
import com.wp.android_base.base.utils.log.Logger;

import java.util.List;

/**
 * Created by wangpeng on 2018/6/25.
 */

public class HttpApiTestActivity extends BaseActivity {

    private TextView mTxClose;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    protected void initializeView() {
        super.initializeView();

        mTxClose = findViewById(R.id.tx_close);

        GitUserApiImpl.testApiLifecycle(this).subscribe(new HttpRequestManager.SimpleObserver<HttpResult<GitUser>>(this) {
            @Override
            protected void onSuccess(HttpResult<GitUser> httpResult) {
                if(httpResult.getCode() == CodeConstant.CODE_UNAUTHORIZED){
                    LoginActivity.forward2Login(HttpApiTestActivity.this);
                }
            }

            @Override
            protected void onError(ApiException.ResponseThrowable responseThrowable) {

            }
        });


        GitUserApiImpl.getGitUser(this, "octocat").subscribe(new HttpRequestManager.SimpleObserver<HttpResult<List<GitUser>>>(this) {
            @Override
            protected void onSuccess(HttpResult<List<GitUser>> listHttpResult) {

            }

            @Override
            protected void onError(ApiException.ResponseThrowable responseThrowable) {

            }
        });


        HttpRequestManager
                .createApi(GitUserApi.class)
                .testNullApiImpl("octocat")
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult>(this) {
                    @Override
                    protected void onSuccess(HttpResult httpResult) {

                    }

                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {

                    }
                });
    }

    public void close(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e("HttpApiTestActivity>>onDestroy", "HttpApiTestActivity>>onDestroy");
    }
}
