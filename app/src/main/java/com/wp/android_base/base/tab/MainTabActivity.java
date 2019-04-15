package com.wp.android_base.base.tab;

import android.app.IntentService;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wp.android_base.MainTabFragment;
import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.tab.model.TabBean;
import com.wp.android_base.base.utils.CheckDataUtil;
import com.wp.android_base.base.utils.ToastUtil;
import com.wp.android_base.base.utils.log.Logger;
import com.wp.android_base.base.widget.viewpager.MainTabViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wp on 2019/4/4.
 * <p>
 * Description:项目的主界面
 */

public class MainTabActivity extends BaseTabActivity {

    final String TAG = "MainTabActivity";

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main_tab;
    }

    @Override
    protected List<TabBean> createTabBeans() {
        LocalBroadcastManager
        String[] titles = getResources().getStringArray(R.array.tabs);
        if (CheckDataUtil.hasData(titles)) {
            List<TabBean> tabBeans = new ArrayList<>();
            for (int i = 0; i < titles.length; i++) {
                TabBean tabBean = new TabBean(titles[i]);
                tabBeans.add(tabBean);
            }
            return tabBeans;
        }
        return null;
    }

    @Override
    protected List<BaseTabFragment> createFragments(List<TabBean> tabBeans) {
        if (CheckDataUtil.hasData(tabBeans)) {
            List<BaseTabFragment> fragments = new ArrayList<>();
            for (int i = 0; i < tabBeans.size(); i++) {
                MainTabFragment mainTabFragment = new MainTabFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("page", i + 1);
                mainTabFragment.setArguments(bundle);
                fragments.add(mainTabFragment);
            }
            return fragments;
        }
        return null;
    }


    @Override
    protected int getCustomTabLayout() {
        return R.layout.view_custom_main_tab;
    }

    @Override
    protected void onTabClick(TabLayout.Tab tab, int position) {
        Logger.e(TAG, "Click:" + position);
        ToastUtil.show(String.valueOf(position + 1));
        tab.select();
    }


    @Override
    protected OnTabSelectListener createTabSelectListener() {
        return new OnTabSelectListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ToastUtil.show("选中" + tab.getPosition());
                View v = tab.getCustomView();
                if (v == null) {
                    return;
                }
                TextView textView = v.findViewById(R.id.tx_tab_title);
                if (textView != null) {
                    textView.setTextSize(16);
                    textView.setTypeface(null, Typeface.BOLD);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                if (v == null) {
                    return;
                }
                TextView textView = v.findViewById(R.id.tx_tab_title);
                if (textView != null) {
                    textView.setTextSize(14);
                    textView.setTypeface(null, Typeface.NORMAL);
                }
            }
        };
    }
}
