package com.wp.android_base.test.tab

import com.wp.android_base.base.tab.BaseTabActivity
import com.wp.android_base.base.tab.BaseTabFragment
import com.wp.android_base.base.tab.model.TabBean

/**
 *
 * Created by wp on 2019/11/15.
 *
 * Description:
 *
 */
class MutTabsActivity :BaseTabActivity(){
    override fun getContentLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createTabBeans(): MutableList<TabBean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createFragments(tabBeans: MutableList<TabBean>?): MutableList<BaseTabFragment> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}