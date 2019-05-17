package com.wp.android_base.base.widget.dialog;

import android.view.Gravity;

import com.wp.android_base.R;
import com.wp.android_base.base.utils.ScreenUtil;
import com.wp.android_base.base.widget.dialog.base.BaseDialog;
import com.wp.android_base.base.widget.dialog.base.DialogPaddingParams;


/**
 * Created by wangpeng on 2019/1/15.
 * <p>
 * Description:
 */

public class ProgressDialog extends BaseDialog {

    public ProgressDialog(){
        mCanceledTouchOutside = false;
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.dialog_progress;
    }

    @Override
    protected int getWindowAnimation() {
        return 0;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.Progress_Dialog;
    }

    @Override
    protected int getDialogLocation() {
        return Gravity.CENTER;
    }

    @Override
    protected DialogPaddingParams createDialogPaddingParams() {
        return null;
    }

    @Override
    protected void resetWH() {
        super.resetWH();
        int dialogWidth = ScreenUtil.dp2px(120); // specify a value here
        int dialogHeight = ScreenUtil.dp2px(120); // specify a value here
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }
}
