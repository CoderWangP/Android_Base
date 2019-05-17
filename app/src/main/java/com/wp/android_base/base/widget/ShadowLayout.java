package com.wp.android_base.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.wp.android_base.R;

/**
 * Created by wangpeng on 2018/7/24.
 */

public class ShadowLayout extends FrameLayout{

    private Paint mShadowPaint;
    /**
     * 阴影x轴偏移
     */
    private float mShadowDx;
    /**
     * 阴影y轴偏移
     */
    private float mShadowDy;
    /**
     * 阴影的颜色
     */
    private int mShadowColor = Color.TRANSPARENT;
    /**
     * z轴的高度，决定了阴影范围的大小
     */
    private float mShadowRadius = 0;
    /**
     * 阴影的ReactF
     */
    private RectF mShadowRectF;

    public ShadowLayout(@NonNull Context context) {
        this(context,null);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(R.styleable.ShadowLayout);
        mShadowColor = array.getColor(R.styleable.ShadowLayout_shadow_color,mShadowColor);
        mShadowDx = array.getDimension(R.styleable.ShadowLayout_shadow_dx,mShadowDx);
        mShadowDy = array.getDimension(R.styleable.ShadowLayout_shadow_dy,mShadowDy);
        mShadowRadius = array.getDimension(R.styleable.ShadowLayout_shadow_radius,mShadowRadius);
        array.recycle();

        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        setWillNotDraw(false);

        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setShadowLayer(mShadowRadius,mShadowDx,mShadowDy,mShadowColor);

        mShadowRectF = new RectF();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float shadowRectLeft = mShadowRadius;
        float shadowRectTop = mShadowColor;
        float shadowRectRight = right - mShadowRadius;
        float shadowRectBottom = bottom - mShadowRadius;

        int paddingLeft = (int) mShadowRadius;
        int paddingTop = (int) mShadowRadius;
        int paddingRight = (int) mShadowRadius;
        int paddingBottom = (int) mShadowRadius;

        if(mShadowDx > 0){
            shadowRectLeft += mShadowDx;
//            shadowRectRight
        }
    }
}
