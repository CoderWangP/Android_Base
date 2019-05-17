package com.wp.android_base.base.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;


/**
 *  Created by wangpeng on 2018/6/21.
 */
public class AppModule {
    private static Application sApplication;

    private AppModule(){}

    public static void init(Application application) {
        sApplication = application;
    }

    public static Application provideApplication() {
        return sApplication;
    }

    public static Context provideContext() {
        return sApplication.getApplicationContext();
    }


    public static Resources provideResources() {
        return sApplication.getResources();
    }
}
