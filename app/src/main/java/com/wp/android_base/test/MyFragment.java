package com.wp.android_base.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseFragment;

/**
 * Created by wangpeng on 2018/8/31.
 * <p>
 * Description:
 */

public class MyFragment extends BaseFragment{
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getContentLayoutId(),container,false);
        return v;
    }
}
