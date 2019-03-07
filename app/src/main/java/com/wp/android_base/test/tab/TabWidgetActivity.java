package com.wp.android_base.test.tab;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.ScreenUtil;
import com.wp.android_base.base.widget.tab.TabWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangpeng on 2018/8/7.
 * <p>
 * Description:
 * <p>
 * CoordinatorLayout：是一个FrameLayout
 * AppBarLayout：
 * 是一个vertical的LinearLayout，其子View应通过setScrollFlags(int)或者xmL中的app:layout_scrollFlags来提供他们的Behavior。
 * 具体的app:layout_scrollFlags有这么几个： scroll, exitUntilCollapsed, enterAlways, enterAlwaysCollapsed, snap
 * 他必须严格地是CoordinatorLayout的子View，不然他一点作用都发挥不出来。
 * AppBarLayout下方的滑动控件，比如RecyclerView，NestedScrollView（与AppBarLayout同属于CoordinatorLayout的子View,并列的关系，）,必须严格地通过在xml中指出其滑动Behavior来与AppBarLayout进行绑定。通常这样：app:layout_behavior="@string/appbar_scrolling_view_behavior"
 * <p>
 * <p>
 * 与ViewPager连用时，ViewPager的item布局中必须要有一个可滑动的布局
 * 包含可滑动的布局内容(RecyclerView,NestedScrollView,不支持ListView，ScrollView)
 * 必须要设置app:layout_behavior="@string/appbar_scrolling_view_behavior"
 * 属性来告知CoordinatorLayout该组件是带有滑动行为的组件,
 * 然后CoordinatorLayout在接受到滑动时会通知AppBarLayout中可滑动的Toolbar可以滑出屏幕，
 * <p>
 * <p>
 * 这个滑动布局RecyclerView 可以写在ViewPager的fragment的item的布局中
 */

public class TabWidgetActivity extends BaseActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_tabwidget;
    }

    @Override
    protected void initializeView() {
        super.initializeView();

        List<String> tabTitles = Arrays.asList(new String[]{"tab1", "tab222222222222", "tab333", "tab4", "tab55555", "tab6", "tab7", "tab8"});

//        List<String> tabTitles = Arrays.asList(new String[]{"tab1", "tab22222222222222222222222"});

        List<TabFragment> fragments = new ArrayList<>();
        for (int i = 0; i < tabTitles.size(); i++) {
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", tabTitles.get(i));
            tabFragment.setArguments(bundle);
            fragments.add(tabFragment);
        }
        TabFragmentsAdapter<TabFragment> tabFragmentsAdapter = new TabFragmentsAdapter(getSupportFragmentManager(), fragments);
        TabWidget tabLayout = findViewById(R.id.tab_layout);
/*        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,R.drawable.vertical_gray_line));
        linearLayout.setDividerPadding(ScreenUtil.dp2px(15));*/
        ViewPager vpWithTab = findViewById(R.id.vp_with_tab);
        vpWithTab.setOffscreenPageLimit(2);
        vpWithTab.setAdapter(tabFragmentsAdapter);
        tabLayout.setupWithViewPager(vpWithTab);

/*        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.view_custom_tab);
            if (i == 0) {
                tab.getCustomView().setSelected(true);
            }
            TextView txTab = tab.getCustomView().findViewById(R.id.tx_custom_tab_position);
            txTab.setText(tabTitles.get(i));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tx_custom_tab_position).setSelected(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tx_custom_tab_position).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }
}
