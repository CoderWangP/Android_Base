package com.wp.android_base.test.recyclerview

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wp.android_base.R
import com.wp.android_base.base.BaseActivity
import com.wp.android_base.base.widget.recyclerview.decoration.StickItemDecoration
import kotlinx.android.synthetic.main.activity_stick_recyclerview.*
import kotlinx.android.synthetic.main.recycler_view_stick.view.*
import kotlin.math.floor

/**
 *
 * Created by wp on 2020/7/1.
 *
 * Description:
 *
 */
class StickRecyclerViewActivity :BaseActivity(){

    private lateinit var mDatas:MutableList<StickBean>

    override fun getContentLayoutId(): Int {
        return R.layout.activity_stick_recyclerview
    }

    override fun initializeView() {
        super.initializeView()
        val linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_stick.layoutManager = linearLayoutManager

        val stickItemDecoration = StickItemDecoration()
        rv_stick.addItemDecoration(stickItemDecoration)
    }

    override fun requestDatas() {
        super.requestDatas()
        mDatas = mutableListOf()
        for(i in 1..10){
            val random = floor(Math.random() * 6).toInt() + 1
            for(j in 1..random){
                val stickBean = StickBean()
                stickBean.group = "第$i 组"
                stickBean.child = "$i 组 第$j 个"
                mDatas.add(stickBean)
            }
        }
        rv_stick.adapter = MyAdapter()
    }


    inner class MyAdapter :RecyclerView.Adapter<MyAdapter.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@StickRecyclerViewActivity).inflate(R.layout.recycler_view_stick,parent,false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return mDatas.size
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val stickBean = mDatas[position]
            if(position == 0){
                viewHolder.itemView.id_stick_header.visibility = View.VISIBLE
                viewHolder.itemView.id_stick_header.text = stickBean.group
            }else{
                val lastStickBean = mDatas[position - 1]
                val lastGroup  = lastStickBean.group
                if(lastGroup == stickBean.group){
                    viewHolder.itemView.id_stick_header.visibility = View.GONE
                }else{
                    viewHolder.itemView.id_stick_header.visibility = View.VISIBLE
                    viewHolder.itemView.id_stick_header.text = stickBean.group
                }
            }
            viewHolder.itemView.tx_group_child.text = stickBean.child
        }

        inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    }
}