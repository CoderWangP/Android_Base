package com.wp.android_base.test.base.dialog;

import android.view.Gravity;

import com.wp.android_base.R;
import com.wp.android_base.base.widget.dialog.base.BaseDialog;

/**
 * Created by wangpeng on 2018/7/23.
 */

public class MyDialogFragment extends BaseDialog {

    @Override
    protected int getDialogLocation() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.dialog_my_dialog;
    }
}
