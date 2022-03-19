package com.mop.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.mop.base.base.fragment.BaseVBFragment
import com.mop.base.util.bus.LiveDataBus
import com.mop.ui.R
import com.mop.ui.databinding.FragmentHomeDrewerBinding
import com.mop.ui.viewmodel.EmptyVM
import kotlinx.android.synthetic.main.fragment_home_drewer.*


class HomeDrawerFragment : BaseVBFragment<FragmentHomeDrewerBinding, EmptyVM>() {

    override fun initView(savedInstanceState: Bundle?) {
        onclick()
        LiveDataBus.observe(this, "XXX", Observer { result ->

        })
    }


    override fun initData() {
        setUserInfo()
    }

    private fun setUserInfo() {
        activity?.let {
            Glide.with(it).load("https://avatars.githubusercontent.com/u/30459094?v=4")
                .apply(
                    RequestOptions().placeholder(R.drawable.find_default_header)
                        .error(R.drawable.find_default_header).transform(CircleCrop())
                ).into(drewerHeader)
        }
        tvName.text = "灭霸"
    }

    fun onclick() {
        //推出抽屉
        drewerClose.setOnClickListener {
            LiveDataBus.send("openDrawer", 1)
        }

        //跳转设置页面
        lay6.setOnClickListener {
            ARouter.getInstance()
                .build("/ui/SettingActivity")
                .greenChannel()
                .navigation()
        }
    }
}