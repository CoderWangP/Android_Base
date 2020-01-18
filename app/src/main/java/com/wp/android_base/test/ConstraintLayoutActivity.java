package com.wp.android_base.test;

import android.support.v7.widget.AppCompatTextView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.widget.textview.AutofitTextView;

import java.util.Random;

/**
 * Created by wp on 2019/4/30.
 * <p>
 * Description:
 */

public class ConstraintLayoutActivity extends BaseActivity{

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_constraint;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        AppCompatTextView textView = findViewById(R.id.tx_auto);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int random = 1 + (int) (Math.random() * (10 -1 + 1));
                StringBuffer sb = new StringBuffer();
                for(int i=0;i<random;i++){
                    sb.append("text");
                }
                textView.setText(sb.toString());
                textView.requestLayout();
            }
        });
    }
}
