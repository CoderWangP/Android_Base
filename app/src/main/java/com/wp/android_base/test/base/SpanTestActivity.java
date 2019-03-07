package com.wp.android_base.test.base;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.FontUtil;
import com.wp.android_base.base.utils.ScreenUtil;
import com.wp.android_base.base.utils.ToastUtil;
import com.wp.android_base.base.widget.span.CenterImageSpan;
import com.wp.android_base.base.widget.span.CustomTypeFaceSpan;
import com.wp.android_base.base.widget.span.click.ClickableImageSpan;
import com.wp.android_base.base.widget.span.click.ClickableMovementMethod;

/**
 * Created by wangpeng on 2019/2/28.
 * <p>
 * Description:
 */

public class SpanTestActivity extends BaseActivity{

    private TextView mTxImageTextSpan;
    private TextView mTxClickImageSpan;

    private TextView mTxCustomTypeFaceSpan;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_span_test;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mTxImageTextSpan = findViewById(R.id.tx_image_and_text);
        mTxClickImageSpan = findViewById(R.id.tx_click_image);
        mTxCustomTypeFaceSpan = findViewById(R.id.tx_custom_type_face_span);
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();

        SpannableString spannableString1 = new SpannableString(" " + "这是一个图文混排并且图片居中的span");
        Drawable drawable = ContextCompat.getDrawable(this,R.mipmap.ic_launcher);
        spannableString1.setSpan(new CenterImageSpan(drawable,ScreenUtil.dp2px(25),ScreenUtil.dp2px(25)),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTxImageTextSpan.setText(spannableString1);


        SpannableString spannableString2 = new SpannableString(" " + "这是一个图文混排并且图片可以点击的span");
        Drawable drawable1 = ContextCompat.getDrawable(this,R.mipmap.ic_launcher);
        ClickableImageSpan clickableImageSpan = new ClickableImageSpan(drawable1) {
            @Override
            public void onClick(View view) {
                ToastUtil.show("图片被点击");
            }
        };
        spannableString2.setSpan(clickableImageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTxClickImageSpan.setText(spannableString2);
        mTxClickImageSpan.setMovementMethod(ClickableMovementMethod.getInstance());
        mTxClickImageSpan.setHighlightColor(getResources().getColor(android.R.color.transparent));

        SpannableString spannableString3 = new SpannableString("这是自定义字体" + "这是正常字体");
        spannableString3.setSpan(new CustomTypeFaceSpan(FontUtil.getTypeface(this)),0,7,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTxCustomTypeFaceSpan.setText(spannableString3);
    }
}
