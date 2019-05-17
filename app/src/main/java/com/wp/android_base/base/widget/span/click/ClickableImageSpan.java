package com.wp.android_base.base.widget.span.click;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.wp.android_base.base.widget.span.CenterImageSpan;

/**
 * Created by wangpeng on 2019/2/28.
 * 图文混排，image与文字居中,而且图片可点击，注意与ClickableMovementMethod连用
 */
public abstract class ClickableImageSpan extends CenterImageSpan {

    /**
     * 注意一定要设置drawable的宽高范围
     * @param drawable
     */
    public ClickableImageSpan(Drawable drawable) {
        super(drawable);
    }

    /**
     * 注意一定要设置drawable的宽高范围
     * @param drawable
     * @param drawableWidth
     * @param drawableHeight
     */
    public ClickableImageSpan(Drawable drawable,int drawableWidth,int drawableHeight) {
        super(drawable,drawableWidth,drawableHeight);
    }

    public abstract void onClick(View view);
}