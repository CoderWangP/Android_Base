package com.wp.android_base.test.base.dialog;

import android.view.View;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.widget.dialog.ProgressDialog;
import com.wp.android_base.base.widget.dialog.base.BaseAlertDialog;
import com.wp.android_base.base.widget.dialog.base.BaseDialog;

/**
 * Created by wangpeng on 2018/7/23.
 */

public class DialogTestActivity extends BaseActivity {


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_dialog_test;
    }


    public void showDialog(View v) {
        MyDialogFragment myDialogFragment = new MyDialogFragment();
        myDialogFragment.show(getSupportFragmentManager());
    }


    public void showAlertDialog(View v) {

        BaseAlertDialog baseAlertDialog = new BaseAlertDialog();
        baseAlertDialog.setTips("测试alert弹窗");
        baseAlertDialog.setAlertDialogClickListener(new BaseAlertDialog.SimpleAlertClickListener() {
            @Override
            public void onPositiveClick(BaseDialog baseDialog) {
                super.onPositiveClick(baseDialog);
            }
        });
        baseAlertDialog.show(getSupportFragmentManager());
    }

    public void showProgressDialog(View v){
        ProgressDialog progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager());
    }
}
