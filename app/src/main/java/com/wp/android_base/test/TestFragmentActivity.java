package com.wp.android_base.test;

import android.support.v4.app.FragmentTransaction;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;

/**
 * Created by wangpeng on 2018/8/31.
 * <p>
 * Description:
 */

public class TestFragmentActivity extends BaseActivity{

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_test_fragment;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_container,new MyFragment());
        ft.commit();
    }
}
