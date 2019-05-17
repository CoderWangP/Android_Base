package com.wp.android_base.base.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wp.android_base.base.BaseFragment;
import com.wp.android_base.base.utils.log.Logger;

/**
 * Created by wp on 17/12/27.
 * <p/>
 * Description:tab类型Fragment
 */
public abstract class BaseTabFragment extends BaseFragment {

    private static final String TAG = "BaseTabFragment";

    protected boolean mIsVisible;
    protected boolean mIsPrepared;
    protected boolean mHasLoadOnce;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint())
        {
            mIsVisible = true;
            onVisible();
        }
        else
        {
            mIsVisible = false;
            onInVisible();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (mRootView == null)
        {
            mRootView = inflater.inflate(getContentLayoutId(), container, false);
            getBundleData();
            initializeView();
            registerListener();
            mIsPrepared = true;
            //初始化本地数据
            requestDatas();
            //请求网络数据
            loadNetDatas();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null)
        {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    /**
     * 不可见
     */
    private void onInVisible()
    {

    }

    /**
     * 可见
     */
    private void onVisible()
    {
        loadNetDatas();
    }
    /**
     *加载网络数据
     */
    protected void loadNetDatas(){
        if (!mIsPrepared || !mIsVisible || mHasLoadOnce)
        {
            return;
        }
        Logger.e(TAG,"加载网络数据");
        requestNetDatas();
        mHasLoadOnce = true;
    }

    protected abstract void requestNetDatas();
}
