package com.wp.android_base.test.tab

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.wp.android_base.base.tab.BaseTabFragment

/**
 *
 * Created by wp on 2019/11/15.
 *
 * Description:
 *
 */
class DynamicAdapter :FragmentPagerAdapter{

    private val mTabFragments: List<BaseTabFragment>

    constructor(fm:FragmentManager,tabFragments: List<BaseTabFragment>):super(fm){
        this.mTabFragments = tabFragments
    }
    override fun getItem(position: Int): Fragment {
        return mTabFragments[position]
    }

    override fun getCount(): Int {
        return mTabFragments.size
    }
}