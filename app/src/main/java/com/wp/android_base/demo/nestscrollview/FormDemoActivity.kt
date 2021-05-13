package com.wp.android_base.demo.nestscrollview

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.wp.android_base.R
import com.wp.android_base.base.BaseActivity
import com.wp.android_base.base.utils.ScreenUtil
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.recycler_view_account_data.view.*
import kotlinx.android.synthetic.main.recycler_view_form_title.view.*

/**
 *
 * Created by wp on 2020/6/23.
 *
 * Description: 表格
 *
 */
class FormDemoActivity :BaseActivity(){

    private lateinit var mFormData:MutableList<Account>

    private lateinit var mFromTitles:MutableList<String>


    companion object{
        private const val DATA_WIDTH = 100f
        const val TAG = "FormDemoActivity"
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_form
    }

    override fun initializeView() {
        super.initializeView()

        val linearLayoutManager1 = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rv_form_titles.layoutManager = linearLayoutManager1

        val linearLayoutManager2 = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_form_content.layoutManager = linearLayoutManager2
    }

    override fun registerListener() {
        super.registerListener()
        rv_form_titles.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val state = recyclerView.scrollState
                if(state != RecyclerView.SCROLL_STATE_IDLE){
                }
            }
        })
        rv_form_content.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val state = recyclerView.scrollState
                if(state != RecyclerView.SCROLL_STATE_IDLE){
                }
            }
        })
    }

    override fun requestDatas() {
        super.requestDatas()
        mFromTitles = mutableListOf()
        for(i in 0 until 6){
            mFromTitles.add("指标:$i")
        }

        mFormData = mutableListOf()
        for(i in 0 .. 50){
            mFormData.add(Account("account:${i+1}", Array(6) {"data${it + 1}"}))
        }
        rv_form_titles.adapter = FormTitlesAdapter()
        rv_form_content.adapter = DataAdapter()
    }


    inner class FormTitlesAdapter : RecyclerView.Adapter<FormTitlesAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@FormDemoActivity).inflate(R.layout.recycler_view_form_title, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.tx_form_title.text = mFromTitles[position]
        }

        override fun getItemCount(): Int {
            return mFromTitles.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    inner class DataAdapter : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@FormDemoActivity).inflate(R.layout.recycler_view_account_data, parent, false)
            view.ll_datas.removeAllViews()
            for(i in 0 until mFromTitles.size){
                val textData = TextView(this@FormDemoActivity)
                textData.run {
                    gravity = Gravity.CENTER
                    maxLines = 1
                }
                view.ll_datas.addView(textData,LinearLayout.LayoutParams(ScreenUtil.dp2px(DATA_WIDTH),LinearLayout.LayoutParams.MATCH_PARENT))
            }
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            for(i in 0 until holder.itemView.ll_datas.childCount){
                val txData = holder.itemView.ll_datas.getChildAt(i) as TextView
                txData.text = mFormData[position].datas[i]
            }
        }

        override fun getItemCount(): Int {
            return mFormData.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}