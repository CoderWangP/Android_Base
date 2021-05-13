package com.wp.android_base.test.base;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wp.android_base.Constants;
import com.wp.android_base.LoginActivity;
import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.SimpleObserver;
import com.wp.android_base.base.http.ApiException;
import com.wp.android_base.base.http.CodeConstant;
import com.wp.android_base.base.http.HttpRequestManager;
import com.wp.android_base.api.gituser.Api;
import com.wp.android_base.base.http.HttpResult;
import com.wp.android_base.api.gituser.GitUserApiImpl;
import com.wp.android_base.base.utils.AppModule;
import com.wp.android_base.base.utils.Sp;
import com.wp.android_base.base.utils.ToastUtil;
import com.wp.android_base.model.Balance;
import com.wp.android_base.model.TokenItem;
import com.wp.android_base.model.gituser.GitUser;
import com.wp.android_base.base.utils.log.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangpeng on 2018/6/25.
 */

public class HttpApiTestActivity extends BaseActivity {

    private TextView mTxClose;

    private String TAG = "HttpApiTestActivity";

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    protected void initializeView() {
        super.initializeView();

        mTxClose = findViewById(R.id.tx_close);

        GitUserApiImpl
                .testApiLifecycle(this)
                .delay(3, TimeUnit.SECONDS)
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult<GitUser>>(this) {
            @Override
            protected void onSuccess(HttpResult<GitUser> httpResult) {
   /*             if (httpResult.getCode() == CodeConstant.CODE_UNAUTHORIZED) {
                    LoginActivity.forward2Login(HttpApiTestActivity.this);
                }*/
                Logger.d(TAG,"update ui");
            }

            @Override
            protected void onError(ApiException.ResponseThrowable responseThrowable) {

            }
        });


        /*GitUserApiImpl.getGitUser(this, "octocat").subscribe(new HttpRequestManager.SimpleObserver<HttpResult<List<GitUser>>>(this) {
            @Override
            protected void onSuccess(HttpResult<List<GitUser>> listHttpResult) {

            }

            @Override
            protected void onError(ApiException.ResponseThrowable responseThrowable) {

            }
        });


        HttpRequestManager
                .createApi(Api.class)
                .testNullApiImpl("octocat")
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult>(this) {
                    @Override
                    protected void onSuccess(HttpResult httpResult) {

                    }

                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {

                    }
                });*/
    }

    public void close(View v) {
        Logger.d(TAG,"finish");
        finish();
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();

        HttpRequestManager
                .createApi(Api.class)
                .getBalance(getXWID(), "http://119.23.10.138:1880/res/bch/wallets/balance")
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult<Balance>>(this) {
                    @Override
                    protected void onSuccess(HttpResult<Balance> balanceHttpResult) {

                    }

                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {

                    }
                });
    }

    private String getXWID(){
        String xwid = Sp.from(AppModule.provideContext(), Constants.SP_CONFIG).read().getString("wid",null);
        if(TextUtils.isEmpty(xwid)){
            xwid = "";
        }
        return xwid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e("HttpApiTestActivity>>onDestroy", "HttpApiTestActivity>>onDestroy");
    }
}
