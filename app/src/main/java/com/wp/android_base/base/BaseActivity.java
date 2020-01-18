package com.wp.android_base.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wp.android_base.R;
import com.wp.android_base.base.utils.StatusBarUtil;
import com.wp.android_base.base.utils.language.ContextWrapper;
import com.wp.android_base.base.utils.language.LanguageUtil;

import java.util.Locale;

/**
 * Created by wangpeng on 2018/6/22.
 */

public abstract class BaseActivity extends RxAppCompatActivity {


    @Override
    protected void attachBaseContext(Context newBase) {
        //适配多语言
        Locale newLocale = LanguageUtil.getCurrentAppLangByLocale();
        super.attachBaseContext(ContextWrapper.wrap(newBase, newLocale));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isFullScreen()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        int layoutResId = getContentLayoutId();
        if(layoutResId != 0){
            setContentView(layoutResId);
        }
        setUpStatusBar();
        initializeView();
        registerListener();
        requestDatas();
    }

    protected boolean isFullScreen() {
        return false;
    }

    protected abstract int getContentLayoutId();

    protected void setUpStatusBar() {
//        StatusBarUtil.setTranslucent(this);
/*        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        StatusBarUtil.setTranslucentForImageView(this,0,swipeRefreshLayout);
        StatusBarUtil.setLightMode(this);*/
    }

    protected void initializeView() {
    }

    protected void registerListener() {
    }

    protected void requestDatas() {
    }
}
