package com.wp.android_base.base.widget.textview;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * Created by wp on 2020/1/10.
 * <p>
 * Description:
 */

public class AutoSizeTextView extends AppCompatTextView{

    private int minTextSize;
    private int maxTextSize;
    private int granularity;

    public AutoSizeTextView(Context context) {
        this(context,null);
    }

    public AutoSizeTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoSizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        minTextSize = TextViewCompat.getAutoSizeMinTextSize(this);
        maxTextSize = TextViewCompat.getAutoSizeMaxTextSize(this);
        granularity = Math.max(1,TextViewCompat.getAutoSizeStepGranularity(this));
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        // this method is called on every setText
        disableAutoSizing();
        setTextSize(TypedValue.COMPLEX_UNIT_SP,maxTextSize);
        super.setText(text, type);
        // enable after the view is laid out and measured at max text size
        post(this::enableAutoSizing);
    }

    private void enableAutoSizing() {
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(this,minTextSize,maxTextSize,granularity, TypedValue.COMPLEX_UNIT_SP);
    }

    private void disableAutoSizing() {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(this,TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE);
    }
}
