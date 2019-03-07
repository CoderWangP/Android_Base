package com.wp.android_base.base.utils.language;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/**
 * Created by wangpeng on 2018/12/14.
 * <p>
 * Description:Context包装类，用于app内更新语言(Android N:7.0时，多语言的设置需要更新到Context上，全量更新)等情况
 * 具体可见官方文档<a href="https://developer.android.com/reference/android/content/res/Configuration.html">
 *
 *
 * 在Activity 的{@link com.wp.android_base.base.BaseActivity#attachBaseContext(Context)}方法中引用
 */

public class ContextWrapper extends android.content.ContextWrapper {

    public ContextWrapper(Context base) {
        super(base);
    }

    /**
     * api 24版本（Android 7.0,VERSION_CODES.N），configuration.locale 该属性过时
     * api 17版本context.createConfigurationContext(configuration)与configuration.setLocale一起添加进来
     *
     * @param context
     * @param newLocale
     * @return
     */
    public static ContextWrapper wrap(Context context, Locale newLocale){
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            //context.createConfigurationContext(configuration)在api 17版本与configuration.setLocale一起添加进来的
            context = context.createConfigurationContext(configuration);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            //Configuration.setLocale方法在api 17（Android 4.2,VERSION_CODES.JELLY_BEAN_MR1）添加进来的
            configuration.setLocale(newLocale);
            //该方法在api level在api 17版本添加进来的
            context = context.createConfigurationContext(configuration);
        }else{
            //api 24版本（Android 7.0,VERSION_CODES.N），该属性过时
            configuration.locale = newLocale;
            //该方法在api level25版本被废弃
            resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        }
        return new ContextWrapper(context);
    }
}
