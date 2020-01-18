package com.wp.android_base.test.tab

import com.wp.android_base.R
import com.wp.android_base.base.BaseActivity

/**
 *
 * Created by wp on 2019/11/15.
 *
 * Description:
 *
 */
class DynamicViewPagerActivity :BaseActivity(){

    override fun getContentLayoutId(): Int {
        return R.layout.activity_dynamic_view_pager
    }

    override fun requestDatas() {
        super.requestDatas()
    }
}