package com.wp.android_base.base.tab;

import android.support.design.widget.TabLayout;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.tab.model.TabBean;
import com.wp.android_base.base.utils.CheckDataUtil;
import com.wp.android_base.base.utils.ToastUtil;

import java.util.List;

/**
 * Created by wp on 2018/8/15.
 * <p>
 * Description:tab相关的activity基类
 */

public abstract class BaseTabActivity extends BaseActivity {

    protected TabLayout mTabLayout;
    protected ViewPager mVpWithTab;

    protected List<BaseTabFragment> mFragments;
    protected List<TabBean> mTabBeans;

    @Override
    protected void initializeView() {
        super.initializeView();

        SparseArray<String> array = new SparseArray<>();
        LongSparseArray<String> array1 = new LongSparseArray<>();
        ArrayMap<String,Object> arrayMap = new ArrayMap<>();


        mTabLayout = findViewById(R.id.tab_layout);
        mVpWithTab = findViewById(R.id.vp_with_tab);
        if (mTabLayout == null || mVpWithTab == null) {
            return;
        }
        mVpWithTab.setOffscreenPageLimit(1);
        mTabBeans = createTabBeans();
        mFragments = createFragments(mTabBeans);

        if (CheckDataUtil.hasData(mTabBeans) && CheckDataUtil.hasData(mFragments)
                && mTabBeans.size() == mFragments.size()) {
            TabFragmentsAdapter tabFragmentsAdapter = new TabFragmentsAdapter(getSupportFragmentManager(), mFragments, mTabBeans);
            mVpWithTab.setAdapter(tabFragmentsAdapter);
            /**
             * TabLayout 通过{@link TabFragmentsAdapter#getPageTitle(int)}方法来获取tab的title,
             * 详见{@link TabLayout#populateFromPagerAdapter()} 方法的701行
             */
            mTabLayout.setupWithViewPager(mVpWithTab);

            /**
             * 自定义tab的点击事件,选中事件时，暂时必须给tab设置自定义布局
             */
            int customTabLayout = getCustomTabLayout();
            if (customTabLayout > 0) {
                initCustomTabs(customTabLayout);
                setOnTabClickListener();
                addTabSelectListener();
            }
        }
    }

    /**
     * 添加tab选中器
     */
    private void addTabSelectListener() {
        OnTabSelectListener onTabSelectListener = createTabSelectListener();
        if(onTabSelectListener != null){
            mTabLayout.addOnTabSelectedListener(onTabSelectListener);
        }
    }

    /**
     * 初始化自定义Tab的布局
     * 默认有两个实现，一个text,一个icon
     *
     * @param customTabLayout
     */
    private void initCustomTabs(int customTabLayout) {
        LinearLayout tabStrip = (LinearLayout) mTabLayout.getChildAt(0);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabBean tabBean = mTabBeans.get(i);
            if (tabBean == null) {
                continue;
            }
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab == null) {
                continue;
            }
            if (i == 0) {
                tab.select();
            }
            View v = LayoutInflater.from(this).inflate(customTabLayout, tabStrip, false);
            TextView textView = v.findViewById(R.id.tx_tab_title);
            if (textView != null) {
                textView.setText(tabBean.getTitle());
                //设置tab的text的颜色，是个颜色的selector
                textView.setTextColor(mTabLayout.getTabTextColors());
            }
            ImageView icon = v.findViewById(R.id.image_tab_icon);
            if (icon != null && tabBean.getIcon() > 0) {
                icon.setImageResource(tabBean.getIcon());
            }
            tab.setCustomView(v);
            //初始化时，如果设置了TabSelectListener,这里要主动调一下onTabSelected和onTabUnselected方法
            OnTabSelectListener onTabSelectListener = createTabSelectListener();
            if(onTabSelectListener != null){
                if(tab.isSelected()){
                    onTabSelectListener.onTabSelected(tab);
                }else{
                    onTabSelectListener.onTabUnselected(tab);
                }
            }
        }
    }

    /**
     * 设置Tab的点击事件，拦截TabLayout的默认实现
     */
    private void setOnTabClickListener() {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            try {
                if (tab != null) {
                    View view = tab.getCustomView();
                    if (view == null) {
                        continue;
                    }
                    view.setTag(i);
                    view.setOnClickListener(v -> {
                        int position = (int) v.getTag();
                        onTabClick(tab, position);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 创建tab的bean
     *
     * @return
     */
    protected abstract List<TabBean> createTabBeans();


    /**
     * 创建tab的fragment
     *
     * @return
     */
    protected abstract List<BaseTabFragment> createFragments(List<TabBean> tabBeans);


    /**
     * 自定义tab的布局
     *
     * @return
     */
    protected int getCustomTabLayout() {
        return 0;
    }

    /**
     * tab的点击事件，需要特殊要求的，可以重写
     *
     * @param tab
     * @param position
     */
    protected void onTabClick(TabLayout.Tab tab, int position) {
        ToastUtil.show(String.valueOf(position + 1));
        tab.select();
    }

    /**
     * 创建TabSelected的监听器
     * @return
     */
    protected OnTabSelectListener createTabSelectListener(){
        return null;
    }

    abstract class OnTabSelectListener implements TabLayout.OnTabSelectedListener{

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
}
