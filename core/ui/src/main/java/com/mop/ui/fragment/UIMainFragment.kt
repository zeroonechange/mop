package com.mop.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.angcyo.tablayout.delegate.ViewPager1Delegate
import com.google.android.material.appbar.AppBarLayout
import com.mop.base.base.fragment.BaseVBFragment
import com.mop.base.ext.dp2px
import com.mop.ui.adapter.ExplorePagerAdapter
import com.mop.ui.databinding.FragmentUiMainBinding
import com.mop.ui.viewmodel.UIMainVM
import kotlinx.android.synthetic.main.fragment_ui_main.*
import java.util.*


class UIMainFragment : BaseVBFragment<UIMainVM, FragmentUiMainBinding>(),
    ViewPager.OnPageChangeListener {

    private var pagerAdapter: ExplorePagerAdapter? = null
    private val fragments: ArrayList<Fragment> = ArrayList()
    private var position = 0


    override fun initView(savedInstanceState: Bundle?) {

        fragments.add(Son1Fragment())
        fragments.add(Son2Fragment())
        fragments.add(Son3Fragment())

        pagerAdapter = ExplorePagerAdapter(fragments, this.childFragmentManager)
        viewpager.adapter = pagerAdapter
        viewpager.addOnPageChangeListener(this)

        tablayout.setTabLayoutConfig {
            tabSelectColor = Color.parseColor("#FFFFFF")
            tabDeselectColor = Color.parseColor("#AFB4BE")
            tabEnableTextBold = true
            tabTextMaxSize = dp2px(20f).toFloat()
            tabTextMinSize = dp2px(16f).toFloat()
        }
        // 将viewpager 和 tablayout绑定起来 点击自动滑动
        ViewPager1Delegate.install(viewpager, tablayout)

        tablayout.setCurrentItem(1)
        viewpager.currentItem = 1

        // 监听
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {

            }
        })
    }

    override fun initData() {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}