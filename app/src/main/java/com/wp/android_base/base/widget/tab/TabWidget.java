package com.wp.android_base.base.widget.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.utils.ScreenUtil;

import java.lang.reflect.Field;

/**
 * Created by wangpeng on 2018/9/19.
 * <p>
 * Description:tab控件，继承TabLayout，解决底部指示线宽度，tab之间的margin问题
 */

public class TabWidget extends TabLayout {

    private int mRightMargin = ScreenUtil.dp2px(10);

    public TabWidget(Context context) {
        this(context, null);
    }

    public TabWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabWidget);
        mRightMargin = (int) array.getDimension(R.styleable.TabWidget_right_margin, mRightMargin);
        array.recycle();
        int mode = getTabMode();
        if (mode == MODE_FIXED) {

        } else {
            fixParams();
        }
    }

    private void fixParams() {
        post(new Runnable() {
            @Override
            public void run() {
                //了解源码得知 线的宽度是根据 tabView的宽度来设置的
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) getChildAt(0);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = 0;
                        params.rightMargin = mRightMargin;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
