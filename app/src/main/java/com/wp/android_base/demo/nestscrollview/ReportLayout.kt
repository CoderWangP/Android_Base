package com.wp.android_base.demo.nestscrollview

import android.content.Context
import android.os.Build
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.support.v4.view.NestedScrollingParent2
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.wp.android_base.R
import com.wp.android_base.base.utils.log.Logger

/**
 *
 * Created by wp on 2020/6/24.
 *
 * Description:
 *
 */
class ReportLayout: LinearLayout,NestedScrollingParent2 {

    private val TAG = "ReportLayout"


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
        orientation = HORIZONTAL
    }

    /**
     * @param child : 该NestedScrollingParent子视图
     * @param target: 该NestedScrollingParent下具体嵌套的那个直接可滚动的视图(如RecyclerView)，一般情况下是child,也有在直接可滚动视图外包一些其他父View的
     * @param axes : 滚动的方向 ViewCompat#SCROLL_AXIS_HORIZONTAL, ViewCompat#SCROLL_AXIS_VERTICAL 或者两个值都有
     * @param type : 嵌套滑动的类型，有两种ViewCompat.TYPE_NON_TOUCH fling效果,ViewCompat.TYPE_TOUCH 手势滑动
     */
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        Logger.d(TAG,"child.id = ${child.id}","target.id = ${target.id}","axes = $axes","type = $type")
        return true
    }

    /**
     * 当 onStartNestedScroll 返回true时，该方法才会被调用
     */
    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Logger.d(TAG,"child.id = ${child.id}","target.id = ${target.id}")
        if(target.tag as? String == "account"){
            Logger.d(TAG,"scroll left")
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Logger.d(TAG,"target.id = ${target.id}","dx = $dx","dy = $dy")
        consumed[0] = 0
        consumed[1] = dy
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        Logger.d(TAG,"target.id = ${target.id}","dxConsumed = $dxConsumed","dyConsumed = $dyConsumed",
                "dxUnconsumed = $dxUnconsumed","dyUnconsumed = $dyUnconsumed")
    }

    override fun onStopNestedScroll(target: View, type: Int) {
    }

}