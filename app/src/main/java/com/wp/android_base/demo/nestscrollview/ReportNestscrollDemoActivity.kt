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
import com.wp.android_base.base.utils.log.Logger
import kotlinx.android.synthetic.main.activity_report_nestscroll.*
import kotlinx.android.synthetic.main.recycler_view_account.view.*
import kotlinx.android.synthetic.main.recycler_view_account_data.view.*

/**
 *
 * Created by wp on 2020/6/23.
 *
 * Description:
 *
 */
class ReportNestscrollDemoActivity :BaseActivity(){

    private lateinit var mAccountData:MutableList<Account>

    private lateinit var mHeaders:MutableList<String>


    companion object{
        private const val DATA_WIDTH = 100f
        const val TAG = "ReportNestscrollDemoActivity"
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_report_nestscroll
    }

    override fun initializeView() {
        super.initializeView()

        val linearLayoutManager2 = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_account.layoutManager = linearLayoutManager2

        val linearLayoutManager3 = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_data.layoutManager = linearLayoutManager3
    }

    override fun registerListener() {
        super.registerListener()
        rv_account.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val state = recyclerView.scrollState
                if(state != RecyclerView.SCROLL_STATE_IDLE){
                    rv_data.scrollBy(dx,dy)
                }
            }
        })
        rv_data.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val state = recyclerView.scrollState
                if(state != RecyclerView.SCROLL_STATE_IDLE){
                    rv_account.scrollBy(dx,dy)
                }
            }
        })

        hs_data.setOnScrollChangedListener(object :NestHScrollView.OnScrollChangeListener{
            override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
                val v: View = hs_data.getChildAt(0)
                if (v.width <= hs_data.scrollX + hs_data.measuredWidth) {
                    Logger.d(TAG,"Scroll to right")
                } else {
                    Logger.d(TAG,"Not scroll to right")
                }
            }
        })
    }

    override fun requestDatas() {
        super.requestDatas()
        mHeaders = mutableListOf()
        for(i in 0 until 6){
            mHeaders.add("指标:$i")
        }
        createHeaderTitlesLayout()

        mAccountData = mutableListOf()
        for(i in 0 .. 50){
            mAccountData.add(Account("account:${i+1}", Array(6) {"data${it + 1}"}))
        }
        rv_account.adapter = AccountAdapter()
        rv_data.adapter = DataAdapter()
    }

    private fun createHeaderTitlesLayout() {
        ll_headers.removeAllViews()
        for(i in mHeaders.indices){
            val title = mHeaders[i]
            val view = createTitleView(title)
            ll_headers.addView(view,LinearLayout.LayoutParams(ScreenUtil.dp2px(DATA_WIDTH),LinearLayout.LayoutParams.MATCH_PARENT))
        }
    }

    private fun createTitleView(title: String): TextView {
        val titleView = TextView(this)
        with(titleView){
            text = title
            gravity = Gravity.CENTER
        }
        return titleView
    }

    inner class AccountAdapter : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@ReportNestscrollDemoActivity).inflate(R.layout.recycler_view_account, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.tx_account.text = mAccountData[position].name
        }

        override fun getItemCount(): Int {
            return mAccountData.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    inner class DataAdapter : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@ReportNestscrollDemoActivity).inflate(R.layout.recycler_view_account_data, parent, false)
            view.ll_datas.removeAllViews()
            for(i in 0 until mHeaders.size){
                val textData = TextView(this@ReportNestscrollDemoActivity)
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
                txData.text = mAccountData[position].datas[i]
            }
        }

        override fun getItemCount(): Int {
            return mAccountData.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}