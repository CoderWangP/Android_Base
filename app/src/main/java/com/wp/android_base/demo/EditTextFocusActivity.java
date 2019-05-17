package com.wp.android_base.demo;

import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.log.Logger;

/**
 * Created by wp on 2019/5/10.
 * <p>
 * Description:点击别的区域让EditText失去焦点，需要让最外层的布局通过点击获取焦点，可以
 *             在布局中添加  android:focusable="true"
 *                         android:focusableInTouchMode="true"两个属性
 */

public class EditTextFocusActivity extends BaseActivity{

    private static final String TAG = "EditTextFocusActivity";
    private EditText mEt1;
    private EditText mEt2;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_edit_text_focus;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mEt1 = findViewById(R.id.et_1);
        mEt2 = findViewById(R.id.et_2);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float rawX = ev.getRawX();
        float rawY = ev.getRawY();
        View v = getCurrentFocus();
        if(v != null){
            RectF rectF = calcViewScreenLocation(v);
            if(!rectF.contains(rawX,rawY)){
                //点击区域不在当前获取焦点的View
                Logger.e(TAG,"点击区域不在当前获取焦点的View内");
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void registerListener() {
        super.registerListener();
        mEt1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Logger.e(TAG,"et-1获取焦点");
                }else{
                    Logger.e(TAG,"et-1失去焦点");
                }
            }
        });

        mEt2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Logger.e(TAG,"et-2获取焦点");
                }else{
                    Logger.e(TAG,"et-2失去焦点");
                }
            }
        });
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    private RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }
}
