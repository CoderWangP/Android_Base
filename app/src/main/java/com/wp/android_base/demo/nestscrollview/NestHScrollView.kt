package com.wp.android_base.demo.nestscrollview

import android.content.Context
import android.os.Build
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import kotlin.math.abs

/**
 *
 * Created by wp on 2020/8/3.
 *
 * Description:
 *
 */
class NestHScrollView : HorizontalScrollView {

    private val TAG = "ReportLayout"

    private var mDownX = 0f
    private var mDownY = 0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)


    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr, 0)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor (context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    }


    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        mOnScrollChangeListener?.onScrollChanged(l, t, oldl, oldt)
    }

    private var mOnScrollChangeListener: OnScrollChangeListener? = null


    interface OnScrollChangeListener {
        fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int);
    }

    fun setOnScrollChangedListener(onScrollChangeListener: OnScrollChangeListener?) {
        mOnScrollChangeListener = onScrollChangeListener
    }
}