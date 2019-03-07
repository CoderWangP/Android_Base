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
            initializeView();
            registerListener();
            mIsPrepared = true;
            getBundleData();
            requestDatas();
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
        requestNetDatas();
    }

    @Override
    protected void requestDatas() {
        requestNetDatas();
    }

    /**
     *加载网络数据
     */
    protected void requestNetDatas(){
        if (!mIsPrepared || !mIsVisible || mHasLoadOnce)
        {
            return;
        }
        Logger.e(TAG,"加载网络数据");
        loadNetData();
        mHasLoadOnce = true;
    }

    protected abstract void loadNetData();
}
