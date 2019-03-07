package com.wp.android_base.test.base;

import android.content.Context;
import android.content.Intent;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.ToastUtil;
import com.wp.android_base.base.utils.log.Logger;

/**
 * Created by wangpeng on 2018/6/26.
 */

public class LogTestActivity extends BaseActivity{


    public static void forward2LoginTest(Context context){
        Intent intent = new Intent(context,LogTestActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_third;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        logTest();
    }

    private void logTest() {
        ToastUtil.show("显示toast");
        Logger.e("tag", "ccc", "ddd", "eee");
    }
}
