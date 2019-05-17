package com.wp.android_base.base.widget.banner;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wangpeng on 2018/7/18.
 * banner的adapter
 */

public abstract class BaseBannerPagerAdapter<T> extends PagerAdapter{

    private List<T> mBannerItemDatas;
    private int base = 10000;

    public void setBannerItemDatas(List<T> bannerItemDatas){
        this.mBannerItemDatas = bannerItemDatas;
    }

    @Override
    public int getCount() {
        if(mBannerItemDatas == null || mBannerItemDatas.size() <= 0){
            return 0;
        }else if(mBannerItemDatas.size() == 1){
            return 1;
        }else{
//            return mBannerItemDatas.size() * base;
            return mBannerItemDatas.size();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int pageLayoutId = getPageLayoutId();
        if(pageLayoutId == 0){
            throw new RuntimeException("请返回有效的layoutId");
        }
        View view = LayoutInflater.from(container.getContext()).inflate(pageLayoutId,container,false);
        int targetPosition = position % mBannerItemDatas.size();
        T t = mBannerItemDatas.get(targetPosition);
        if(t != null){
            onBindViewData(targetPosition,view,t);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    protected abstract void onBindViewData(int position, View view, T t);
    protected abstract int getPageLayoutId();
}
