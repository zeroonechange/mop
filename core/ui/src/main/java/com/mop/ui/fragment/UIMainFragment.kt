package com.mop.ui.fragment

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.angcyo.tablayout.delegate.ViewPager1Delegate
import com.google.android.material.appbar.AppBarLayout
import com.mop.base.ui.DataBindingBaseFragment
import com.mop.base.utils.DensityUtil
import com.mop.ui.BR
import com.mop.ui.R
import com.mop.ui.adapter.ExplorePagerAdapter
import com.mop.ui.databinding.FragmentUiMainBinding
import com.mop.ui.viewmodel.UIMainVM
import java.util.*


class UIMainFragment : DataBindingBaseFragment<FragmentUiMainBinding, UIMainVM>(
    R.layout.fragment_ui_main,
    BR.viewModel
) , ViewPager.OnPageChangeListener{

    private var pagerAdapter: ExplorePagerAdapter? = null
    private val fragments: ArrayList<Fragment> = ArrayList()
    private var position = 0

    override fun initData() {
        super.initData()

        fragments.add(Son1Fragment())
        fragments.add(Son2Fragment())
        fragments.add(Son3Fragment())

        pagerAdapter = ExplorePagerAdapter(fragments, this.childFragmentManager)
        mBinding.viewpager.adapter = pagerAdapter
        mBinding.viewpager.addOnPageChangeListener(this)

        mBinding.tablayout.setTabLayoutConfig {
            tabSelectColor = Color.parseColor("#FFFFFF")
            tabDeselectColor = Color.parseColor("#AFB4BE")
            tabEnableTextBold = true
            tabTextMaxSize = DensityUtil.dp2px(20f).toFloat()
            tabTextMinSize = DensityUtil.dp2px(16f).toFloat()
        }
        // 将viewpager 和 tablayout绑定起来 点击自动滑动
        ViewPager1Delegate.install(mBinding.viewpager, mBinding.tablayout)
        mBinding.tablayout.setCurrentItem(1)
        // 监听
        mBinding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener{ appBarLayout, verticalOffset ->
            if(verticalOffset == 0){

            }
        })
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}