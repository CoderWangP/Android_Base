package com.wp.android_base.base.tab;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.wp.android_base.base.BaseActivity;

/**
 * Created by wp on 2018/8/15.
 * <p>
 * Description:
 */

public abstract class BaseTabActivity extends BaseActivity{

    private TabLayout mTabLayout;
    private ViewPager mVpWithTab;

    @Override
    protected void initializeView() {
        super.initializeView();
    }
}
