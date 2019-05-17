package com.wp.android_base.base.widget.span;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Created by wp on 2017/12/12.
 * 自定义字体的Span，可以用{@link com.wp.android_base.base.utils.FontUtil}获取自定义的字体
 * 可以用于SpannableString.setSpan(new CustomTypeFaceSpan(FontUtil.getTypeface(context)),,,)
 * {@link android.text.SpannableString#setSpan(Object,int, int, int) }
 */
public class CustomTypeFaceSpan extends MetricAffectingSpan {

    private final Typeface typeface;


    public CustomTypeFaceSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        apply(p);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        apply(tp);
    }

    private void apply(final Paint paint) {
        final Typeface oldTypeface = paint.getTypeface();
        final int oldStyle = oldTypeface != null ? oldTypeface.getStyle() : 0;
        final int fakeStyle = oldStyle & ~typeface.getStyle();
        if ((fakeStyle & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }
        if ((fakeStyle & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(typeface);
    }
}
