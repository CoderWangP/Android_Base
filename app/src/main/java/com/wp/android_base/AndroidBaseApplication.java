package com.wp.android_base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.wp.android_base.base.http.ApiException;
import com.wp.android_base.base.http.HttpRequestManager;
import com.wp.android_base.api.gituser.Api;
import com.wp.android_base.base.http.HttpResult;
import com.wp.android_base.base.utils.language.ContextWrapper;
import com.wp.android_base.base.utils.language.LanguageUtil;
import com.wp.android_base.base.utils.log.Logger;
import com.wp.android_base.model.gituser.GitUser;
import com.wp.android_base.base.utils.AppModule;

import java.util.List;
import java.util.Locale;

/**
 * Created by wangpeng on 2018/6/21.
 *
 */

public class AndroidBaseApplication extends Application{

    private static final String TAG = "AndroidBaseApplication";

    @Override
    protected void attachBaseContext(Context base) {
        Locale newLocale = LanguageUtil.getCurrentAppLangByLocale(base);
        super.attachBaseContext(ContextWrapper.wrap(base, newLocale).getBaseContext());
        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Locale newLocale = LanguageUtil.getCurrentAppLangByLocale(this);
        updateLanguage(this,newLocale);
        super.onConfigurationChanged(this.getResources().getConfiguration());
        Logger.d(TAG,"onConfigurationChanged");
    }

    private void updateLanguage(Context context, Locale newLocale){
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context = context.createConfigurationContext(configuration);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(newLocale);
//            context = context.createConfigurationContext(configuration);
            resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        }else{
            configuration.locale = newLocale;
            resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        }
    }


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
/*        HttpRequestManager.createApi(Api.class).testNullApiImpl("octocat")
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult>(this) {
                    @Override
                    protected void onSuccess(HttpResult httpResult) {

                    }

                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {

                    }
                });

        HttpRequestManager.createApi(Api.class).getGitUser("octocat")
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult<List<GitUser>>>(this) {
                    @Override
                    protected void onSuccess(HttpResult<List<GitUser>> listHttpResult) {

                    }

                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {

                    }
                });*/
    }

}
