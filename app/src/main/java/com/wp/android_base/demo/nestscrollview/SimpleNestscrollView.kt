package com.wp.android_base.demo.nestscrollview

import android.content.Context
import android.os.Build
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

/**
 *
 * Created by wp on 2020/6/28.
 *
 * Description:
 *
 */
class SimpleNestscrollView :ScrollView{

    constructor(context: Context):this(context,null)

    constructor(context: Context, @Nullable attrs: AttributeSet?): this(context,attrs,0)


    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int):super(context,attrs,defStyleAttr){
        initView(context,attrs,defStyleAttr,0)
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor (context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int, defStyleRes:Int ) :super(context,attrs,defStyleAttr,defStyleRes){
        initView(context,attrs,defStyleAttr,defStyleRes)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }
}