package com.wp.android_base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wp.android_base.base.utils.log.Logger;

/**
 * Created by wp on 2019/5/23.
 * <p>
 * Description:
 */

public class CustomView extends View{

    private static final String TAG = "CustomView";

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(0xffFF0000);

        Logger.e(TAG,"CustomView(Context context, @Nullable AttributeSet attrs)");
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Logger.e(TAG,"onVisibilityChanged");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Logger.e(TAG,"onFinishInflate");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Logger.e(TAG,"onAttachedToWindow");
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Logger.e(TAG,"onWindowFocusChanged");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Logger.e(TAG,"onMeasure");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Logger.e(TAG,"onSizeChanged");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Logger.e(TAG,"onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Logger.e(TAG,"onDraw");
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawText("这是个text",width / 2,height / 2,paint);
    }
}
