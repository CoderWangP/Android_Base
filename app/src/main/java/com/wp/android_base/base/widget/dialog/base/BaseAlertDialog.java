package com.wp.android_base.base.widget.dialog.base;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.utils.ScreenUtil;

/**
 * Created by wangpeng on 2018/7/24.
 * 提示弹窗基类
 */

public class BaseAlertDialog extends BaseDialog {

    private TextView mTxTips;
    private TextView mTxNegative;
    private TextView mTxPositive;

    private CharSequence mTips;
    private CharSequence mNegative;
    private CharSequence mPositive;

    @Override
    protected int getDialogLocation() {
        return Gravity.CENTER;
    }

    @Override
    protected int getWindowAnimation() {
        return 0;
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.dialog_base_alert;
    }

    @Override
    protected void initializeViews(View contentView) {
        super.initializeViews(contentView);
        mTxTips = contentView.findViewById(R.id.tx_base_alert_tips);
        mTxNegative = contentView.findViewById(R.id.dialog_cancel_id);
        mTxPositive = contentView.findViewById(R.id.tx_base_alert_positive);
    }

    @Override
    protected void registerListener() {
        super.registerListener();
        mTxNegative.setOnClickListener(this);
        mTxPositive.setOnClickListener(this);
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();
        if(!TextUtils.isEmpty(mTips)){
            mTxTips.setText(mTips);
        }
        if(!TextUtils.isEmpty(mNegative)){
            mTxNegative.setText(mNegative);
        }
        if(!TextUtils.isEmpty(mPositive)){
            mTxPositive.setText(mPositive);
        }
    }

    public void setTips(CharSequence tips){
        this.mTips = tips;
    }
    public void setNegative(CharSequence negative){
        this.mNegative = negative;
    }
    public void setPositive(CharSequence positive){
        this.mPositive = positive;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel_id:
                if(mAlertDialogClickListener != null){
                    mAlertDialogClickListener.onNegativeClick(this);
                }
                break;
            case R.id.tx_base_alert_positive:
                if(mAlertDialogClickListener != null){
                    mAlertDialogClickListener.onPositiveClick(this);
                }
                break;
        }
    }

    public static abstract class SimpleAlertClickListener implements AlertDialogClickListener {
        @Override
        public void onNegativeClick(BaseDialog baseDialog) {
            if (baseDialog != null) {
                baseDialog.dismiss();
            }
        }

        @Override
        public void onPositiveClick(BaseDialog baseDialog) {
            if(baseDialog != null){
                baseDialog.dismiss();
            }
        }
    }

    public interface AlertDialogClickListener {
        void onNegativeClick(BaseDialog baseDialog);
        void onPositiveClick(BaseDialog baseDialog);
    }

    private AlertDialogClickListener mAlertDialogClickListener;

    public void setAlertDialogClickListener(AlertDialogClickListener alertDialogClickListener) {
        this.mAlertDialogClickListener = alertDialogClickListener;
    }
}
