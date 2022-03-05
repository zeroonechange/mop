package com.mop.ui.fragment

import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.mop.base.mvvm.EmptyVM
import com.mop.base.ui.DataBindingBaseFragment
import com.mop.base.utils.bus.LiveDataBus
import com.mop.ui.BR
import com.mop.ui.R
import com.mop.ui.databinding.FragmentHomeDrewerBinding


class HomeDrawerFragment : DataBindingBaseFragment<FragmentHomeDrewerBinding, EmptyVM>(
    R.layout.fragment_home_drewer,
    BR.viewModel
) {
    override fun initViewModel() {
        super.initViewModel()
        onclick()
        LiveDataBus.observe(this, "XXX", Observer { result ->

        })
    }

    override fun initData() {
        super.initData()
        setUserInfo()
    }

    private fun setUserInfo() {
        activity?.let {
            Glide.with(it).load("https://avatars.githubusercontent.com/u/30459094?v=4")
                .apply(
                    RequestOptions().placeholder(R.drawable.find_default_header)
                        .error(R.drawable.find_default_header).transform(CircleCrop())
                ).into(mBinding.drewerHeader)
        }
        mBinding.tvName.text = "灭霸"
    }

    fun onclick() {
        //推出抽屉
        mBinding.drewerClose.setOnClickListener {
            LiveDataBus.send("openDrawer", 1)
        }

        //跳转设置页面
        mBinding.settingRlt.setOnClickListener {
            ARouter.getInstance()
                .build("/ui/SettingActivity")
                .greenChannel()
                .navigation()
        }
    }
}