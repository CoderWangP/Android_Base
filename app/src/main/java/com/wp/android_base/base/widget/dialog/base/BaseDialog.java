package com.wp.android_base.base.widget.dialog.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.trello.rxlifecycle2.components.support.RxDialogFragment;
import com.wp.android_base.R;
import com.wp.android_base.base.utils.ScreenUtil;

/**
 * Created by wp on 2018/7/23.
 * dialog基类
 */

public abstract class BaseDialog extends RxDialogFragment implements View.OnClickListener {

    private View mCancelDialogView;
    //外部区域是否可消失
    protected boolean mCanceledTouchOutside = true;
    //物理返回键，是否可消失
    protected boolean mCanceledOnPressKeyBack = true;

    public BaseDialog(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = getDialogStyle();
        if(style <= 0){
            style = R.style.Base_Dialog;
        }
        setStyle(DialogFragment.STYLE_NORMAL,style);
    }

    protected int getDialogStyle(){
        return R.style.Base_Dialog;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        if (getDialog() == null){
            return;
        }
        resetWH();
    }

    /**
     * 重新设置宽高
     */
    protected void resetWH(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contentView = inflater.inflate(getContentLayoutId(), container, false);

        Dialog dialog = getDialog();
        if (dialog != null) {

            Window window = dialog.getWindow();

            //可设置dialog的位置
            window.setGravity(getDialogLocation());

            //设置dialog的四周边距,需要dialog按照自己的风格全屏
            DialogPaddingParams paddingParams = createDialogPaddingParams();
            if(paddingParams != null){
                window.getDecorView().setPadding(paddingParams.left, paddingParams.top, paddingParams.right, paddingParams.bottom);
            }
            //设置宽高参数
            WindowManager.LayoutParams lp = window.getAttributes();
            setWindowLayoutParams(lp);
            window.setAttributes(lp);

            //设置动画
            int anim = getWindowAnimation();
            if(anim > 0){
                window.setWindowAnimations(anim);
            }

            dialog.setCancelable(mCanceledTouchOutside);
            //点击外部区域是否可以取消
            dialog.setCanceledOnTouchOutside(mCanceledTouchOutside);
            //点击物理返回键是否可取消
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return !mCanceledOnPressKeyBack;
                    }
                    return false;
                }
            });
        }

        initializeViews(contentView);
        registerListener();
        requestDatas();

        return contentView;
    }

    protected DialogPaddingParams createDialogPaddingParams() {
        DialogPaddingParams paddingParams = new DialogPaddingParams();
        paddingParams.left = ScreenUtil.dp2px(20);
        paddingParams.right = ScreenUtil.dp2px(20);
        return paddingParams;
    }


    protected void initializeViews(View contentView) {
        mCancelDialogView = contentView.findViewById(R.id.dialog_cancel_id);
    }


    protected void registerListener() {
        if (mCancelDialogView != null) {
            mCancelDialogView.setOnClickListener(this);
        }
    }


    protected void requestDatas() {

    }

    @Override
    public void onClick(View v) {
        if (v == mCancelDialogView) {
            dismiss();
        }
    }

    public void setCanceledTouchOutside(boolean canceledTouchOutside) {
        this.mCanceledTouchOutside = canceledTouchOutside;
    }

    public void setCanceledOnPressKeyBack(boolean canceledOnPressKeyBack) {
        this.mCanceledOnPressKeyBack = canceledOnPressKeyBack;
    }



    /**
     * 设置dialog的四周边距
     *
     * @param paddingParams
     */
    protected void setDialogPaddingParams(DialogPaddingParams paddingParams) {
        paddingParams.left = ScreenUtil.dp2px(20);
        paddingParams.right = ScreenUtil.dp2px(20);
    }


    /**
     * 设置dialog的宽高参数
     *
     * @param lp
     * @return
     */
    protected void setWindowLayoutParams(WindowManager.LayoutParams lp) {
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }


    /**
     * dialog显示，消失的动画
     *
     * @return
     */
    protected int getWindowAnimation() {
        return R.style.window_bootom_in_top_out_animation;
    }

    /**
     * dialog的位置
     *
     * @return
     */
    protected int getDialogLocation() {
        return Gravity.BOTTOM;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    protected abstract int getContentLayoutId();

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, getClass().getName());
    }

    public void show(FragmentManager fragmentManager, String tag) {
        try {
            if (isAdded()) {
                //dialog已经被添加过
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            super.show(fragmentManager, getClass().getName());
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void show(FragmentTransaction fragmentTransaction) {
        show(fragmentTransaction, getClass().getName());
    }

    public int show(FragmentTransaction fragmentTransaction, String tag) {
        try {
            if (isAdded()) {
                //dialog已经被添加过
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            return super.show(fragmentTransaction, tag);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void dismiss() {
        try {
            Dialog dialog = getDialog();
            if (dialog != null && dialog.isShowing()) {
                super.dismiss();
                if(mOnDismissListener != null){
                    mOnDismissListener.onDismiss();
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void dismissAllowingStateLoss(){
        if(isShowing()){
            super.dismissAllowingStateLoss();
        }
    }


    public boolean isShowing(){
        Dialog dialog = getDialog();
        return dialog != null && dialog.isShowing();
    }



    private OnDismissListener mOnDismissListener;
    public interface OnDismissListener{
        void onDismiss();
    }

    public void setOnDismissListener(OnDismissListener onDismissListener){
        this.mOnDismissListener = onDismissListener;
    }

}
