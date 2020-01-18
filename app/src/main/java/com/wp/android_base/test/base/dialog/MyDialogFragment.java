package com.wp.android_base.test.base.dialog;

import android.view.Gravity;

import com.wp.android_base.R;
import com.wp.android_base.base.utils.ScreenUtil;
import com.wp.android_base.base.widget.dialog.base.BaseDialog;
import com.wp.android_base.base.widget.dialog.base.DialogPaddingParams;

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

    @Override
    protected int getDialogStyle() {
        return R.style.Full_Screen_Dialog;
    }

    @Override
    protected DialogPaddingParams createDialogPaddingParams() {
        DialogPaddingParams paddingParams = new DialogPaddingParams();
        paddingParams.left = 0;
        paddingParams.right = 0;
        return paddingParams;
    }
}
