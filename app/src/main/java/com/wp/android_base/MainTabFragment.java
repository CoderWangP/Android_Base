package com.wp.android_base;

import android.widget.TextView;

import com.wp.android_base.base.tab.BaseTabFragment;

/**
 * Created by wp on 2019/4/4.
 * <p>
 * Description:
 */

public class MainTabFragment extends BaseTabFragment{

    private TextView mTxPage;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_main_tab;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mTxPage = mRootView.findViewById(R.id.tx_page);
        mTxPage.setText("第" + mBundle.getInt("page") + "页");;
    }

    @Override
    protected void requestNetDatas() {

    }
}
