package com.wp.android_base.test.base;

import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;

import java.io.ObjectInputStream;

/**
 * Created by wp on 2019/6/6.
 * <p>
 * Description:
 */

public class ToastActivity extends BaseActivity{
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_toast;
    }

    public void showToast(View view) {
//        ToastUtil.show("这是一个Toast");
        Toast.makeText(this,"底部",Toast.LENGTH_LONG).show();
        Toast toast = Toast.makeText(this,"中心",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

        for(int i=0;i<50;i++){
            Toast.makeText(this,"这是第" + i + "个",Toast.LENGTH_SHORT).show();
        }
    }
}
