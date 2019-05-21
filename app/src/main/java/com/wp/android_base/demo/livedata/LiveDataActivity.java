package com.wp.android_base.demo.livedata;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStoreOwner;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;

/**
 * Created by wp on 2019/5/14.
 * <p>
 * Description:
 */

public class LiveDataActivity extends BaseActivity{

    private LiveDataTimerViewModel mLiveDataTimerViewModel;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_live_data;
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();
    }
}
