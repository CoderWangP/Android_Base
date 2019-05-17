package com.wp.android_base.base;

import android.os.Bundle;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by wangpeng on 2018/8/7.
 * <p>
 * Description:
 */

public abstract class BaseFragment extends RxFragment {

    protected View mRootView;
    protected Bundle mBundle;


    protected abstract int getContentLayoutId();

    protected void initializeView() {
    }

    protected void registerListener() {
    }

    protected void getBundleData() {
        mBundle = getArguments();
    }

    protected void requestDatas(){
    }
}
