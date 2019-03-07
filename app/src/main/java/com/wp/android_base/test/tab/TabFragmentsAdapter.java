package com.wp.android_base.test.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wp.android_base.base.tab.BaseTabFragment;

import java.util.List;

/**
 * Created by wangpeng on 2018/8/7.
 * <p>
 * Description:
 */

public class TabFragmentsAdapter<T extends BaseTabFragment> extends FragmentPagerAdapter{

    private List<T> mTabFragments;

    public TabFragmentsAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabFragmentsAdapter(FragmentManager fm,List<T> tabFragments){
        super(fm);
        this.mTabFragments = tabFragments;
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
        T t = mTabFragments.get(position);
        Bundle bundle = t.getArguments();
        return bundle.getString("title","default");
    }
}
