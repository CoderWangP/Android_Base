package com.wp.android_base.demo.form

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wp.android_base.R
import com.wp.android_base.base.BaseActivity
import com.wp.android_base.base.utils.log.Logger
import com.wp.android_base.demo.nestscrollview.Account
import kotlinx.android.synthetic.main.activity_form_1.*
import kotlinx.android.synthetic.main.recycler_view_form_content.view.*
import kotlinx.android.synthetic.main.recycler_view_form_right_data.view.*
import kotlinx.android.synthetic.main.recycler_view_header.view.*
import java.lang.ref.WeakReference

/**
 *
 * Created by wp on 2020/8/19.
 *
 * Description:
 *
 */
class FormActivity :BaseActivity(){

    private lateinit var mAccountData:MutableList<Account>

    private lateinit var mHeaders:MutableList<String>

    private lateinit var mFormContentAdapter:FormContentAdapter

    private var mHasMoreData = true
    private var mIsLoadingMore = false

    companion object{
        private const val TAG = "FormActivity"
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_form_1
    }

    override fun initializeView() {
        super.initializeView()
        val linearLayoutManager1 = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rv_headers.layoutManager = linearLayoutManager1
        rv_headers.setHasFixedSize(true)
        val linearLayoutManager2 = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_form_content.layoutManager = linearLayoutManager2
        rv_form_content.setHasFixedSize(true)

        rv_form_content.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                    if(layoutManager != null && layoutManager.orientation == LinearLayoutManager.VERTICAL){
                        if(!recyclerView.canScrollVertically(1)){
                            if(!mIsLoadingMore && mHasMoreData){
                                mIsLoadingMore = true
                                startLoadMore()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun startLoadMore() {
        val size = mAccountData.size
        for(i in size + 1 .. size + 15){
            mAccountData.add(Account("account:${i}", Array(mHeaders.size) {"data$i:${it + 1}"}))
        }
        mIsLoadingMore = false
        if(mAccountData.size >= 60){
            mHasMoreData = false
        }
        mFormContentAdapter.refresh()
    }

    override fun requestDatas() {
        super.requestDatas()
        mHeaders = mutableListOf()
        for(i in 0 until 6){
            mHeaders.add("指标:${i + 1}")
        }
        mAccountData = mutableListOf()
        for(i in 1 .. 15){
            mAccountData.add(Account("account:${i}", Array(mHeaders.size) {"data$i:${it + 1}"}))
        }
        rv_headers.adapter = HeaderAdapter()
        mFormContentAdapter = FormContentAdapter()
        rv_form_content.adapter = mFormContentAdapter
    }


    inner class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@FormActivity).inflate(R.layout.recycler_view_header, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.tx_header.text = mHeaders[position]
        }

        override fun getItemCount(): Int {
            return mHeaders.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    inner class FormContentAdapter : RecyclerView.Adapter<FormContentAdapter.ViewHolder>() {

        private var TAG = "FormContentAdapter"

        /**
         * 联动的横向滚动的RecyclerView
         */
        private var mScrollViews:MutableList<RecyclerView> = mutableListOf()

        fun refresh(){
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@FormActivity).inflate(R.layout.recycler_view_form_content, parent, false)
            val rvData = view.findViewById<RecyclerView>(R.id.rv_form_data)
            Logger.d(TAG,"onCreateViewHolder")
            val linearLayoutManager = LinearLayoutManager(this@FormActivity,LinearLayoutManager.HORIZONTAL,false)
            rvData.layoutManager = linearLayoutManager
            rvData.setHasFixedSize(true)
            rvData.adapter = FormDataAdapter()
            mScrollViews.add(rvData)
            rvData.addOnScrollListener(object :RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    for(scrollView in mScrollViews){
                        if(recyclerView != scrollView){
                            scrollView.scrollBy(dx,dy)
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
            Logger.d(TAG,"itemcount = $itemCount","mScrollViews.size = ${mScrollViews.size}")
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val account = mAccountData[position]
            holder.itemView.tx_form_lock_left.text = account.name
            val layoutManager = holder.itemView.rv_form_data.layoutManager
            if(layoutManager == null){
                Logger.d(TAG,"onBindViewHolder init recycler view")
                val linearLayoutManager = LinearLayoutManager(this@FormActivity,LinearLayoutManager.HORIZONTAL,false)
                holder.itemView.rv_form_data.layoutManager = linearLayoutManager
                holder.itemView.rv_form_data.setHasFixedSize(true)
            }
            var dataAdapter = holder.itemView.rv_form_data.adapter as? FormDataAdapter
            if(dataAdapter == null){
                Logger.d(TAG,"onBindViewHolder init data adapter")
                dataAdapter = FormDataAdapter(account)
                holder.itemView.rv_form_data.adapter = dataAdapter
            }else{
                Logger.d(TAG,"onBindViewHolder refresh data adapter data")
                dataAdapter.setData(account)
                dataAdapter.refresh()
            }
            holder.itemView.setBackgroundColor(if(position % 2 == 0){
                Color.WHITE
            } else Color.GREEN)
            Logger.d(TAG,"onBindViewHolder  ScrollViews.size = ${mScrollViews.size}")

        }

        override fun getItemCount(): Int {
            return mAccountData.size
        }


        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    inner class FormDataAdapter() : RecyclerView.Adapter<FormDataAdapter.ViewHolder>() {

        private var mAccount:Account? = null

        constructor(account: Account):this(){
            this.mAccount = account
        }

        fun setData(account: Account){
            this.mAccount = account
        }

        fun refresh(){
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@FormActivity).inflate(R.layout.recycler_view_form_right_data, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.tx_data1.text = mAccount?.datas?.get(position)
            holder.itemView.tx_data2.text = "T"
        }

        override fun getItemCount(): Int {
            return mAccount?.datas?.size ?: 0
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}