package com.wp.android_base.test.java;

import android.content.Intent;
import android.view.View;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.test.java.clone.TestCloneActivity;

/**
 * Created by wp on 2019/5/31.
 * <p>
 * Description:
 */

public class JavaActivity extends BaseActivity{
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_java;
    }

    public void javaClone(View view) {
        startActivity(new Intent(this,TestCloneActivity.class));
    }
}
