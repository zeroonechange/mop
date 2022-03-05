package com.mop.ui.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

open class ExplorePagerAdapter(private val fragmentList: List<Fragment>?, fm: FragmentManager?) :
    FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return fragmentList!![position]
    }

    override fun getCount(): Int {
        return if (fragmentList != null && fragmentList.isNotEmpty()) fragmentList.size else 0
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

    }
}
