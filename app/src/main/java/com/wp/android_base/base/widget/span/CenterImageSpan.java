package com.wp.android_base.base.widget.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Created by wangpeng on 2019/2/28.
 * 图文混排，image与文字居中
 */
public class CenterImageSpan extends ImageSpan {


    /**
     * 注意一定要设置drawable的宽高范围
     * @param drawable
     */
    public CenterImageSpan(Drawable drawable){
        this(drawable,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
    }

    /**
     * 注意一定要设置drawable的宽高范围
     * @param drawable
     * @param drawableWidth
     * @param drawableHeight
     */
    public CenterImageSpan(Drawable drawable,int drawableWidth,int drawableHeight) {
        this(drawable,ImageSpan.ALIGN_BASELINE,drawableWidth,drawableHeight);
    }

    /**
     * 注意一定要设置drawable的宽高范围
     * @param drawable
     * @param verticalAlignment
     * @param drawableWidth
     * @param drawableHeight
     */
    public CenterImageSpan(Drawable drawable, int verticalAlignment,int drawableWidth,int drawableHeight) {
        super(drawable, verticalAlignment);
        drawable.setBounds(0,0,drawableWidth,drawableHeight);
    }

    /**
     * @param canvas
     * @param text:整个text；
     * @param start:这个Span起始字符在text中的位置
     * @param end:这个Span结束字符在text中的位置；
     * @param x:这个Span的起始水平坐标；
     * @param top：这个Span的起始垂直坐标；
     * @param y：这个Span的baseline的垂直坐标；
     * @param bottom：这个Span的结束垂直坐标
     * @param paint
     */
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2
                - b.getBounds().bottom / 2;
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}