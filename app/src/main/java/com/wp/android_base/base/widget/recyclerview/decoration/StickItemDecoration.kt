package com.wp.android_base.base.widget.recyclerview.decoration

import android.graphics.Canvas
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.wp.android_base.R
import com.wp.android_base.base.utils.log.Logger

/**
 *
 * Created by wp on 2020/7/1.
 *
 * Description:
 *
 */
class StickItemDecoration :RecyclerView.ItemDecoration(){

    private val TAG = "StickItemDecoration"

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        if (childCount <= 0) {
            return
        }
        val layoutManager = parent.layoutManager ?: return
        if(layoutManager is LinearLayoutManager){
            for(i in 0 until childCount){
                val view = parent.getChildAt(i) ?: continue
                if(view is ViewGroup){
                    val stickHeaderView = view.findViewById<View>(R.id.id_stick_header) ?: continue
                    Logger.d(TAG,"position = $i","${stickHeaderView.visibility}")
                    Log.d(TAG,"position = $i \n ${stickHeaderView.visibility}")
                    if(stickHeaderView.visibility == View.VISIBLE){
                        stickHeaderView.draw(c)
                    }
                }
            }
        }
    }
}