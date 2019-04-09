package com.wp.android_base.base.tab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.wp.android_base.base.tab.BaseTabFragment;
import com.wp.android_base.base.tab.model.TabBean;
import com.wp.android_base.base.utils.CheckDataUtil;

import java.util.List;

/**
 * Created by wangpeng on 2018/8/7.
 * <p>
 * Description:
 */

public class TabFragmentsAdapter extends FragmentPagerAdapter{

    private List<BaseTabFragment> mTabFragments;
    private List<TabBean> mTabBeans;

    public TabFragmentsAdapter(FragmentManager fm, List<BaseTabFragment> tabFragments, List<TabBean> tabBeans){
        super(fm);
        this.mTabFragments = tabFragments;
        this.mTabBeans = tabBeans;
    }

    @Override
    public Fragment getItem(int position) {
        return mTabFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTabFragments == null ? 0 :mTabFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(CheckDataUtil.hasData(mTabBeans) && mTabBeans.size() > position){
            TabBean tabBean = mTabBeans.get(position);
            if(tabBean != null){
                return tabBean.getTitle();
            }
        }
        return null;
    }
}
