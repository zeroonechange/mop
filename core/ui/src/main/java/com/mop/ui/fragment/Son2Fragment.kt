package com.mop.ui.fragment

import android.os.Bundle
import com.drake.brv.utils.*
import com.mop.base.base.fragment.BaseDBFragment
import com.mop.base.net.initNetUnsplash
import com.mop.ui.BR
import com.mop.ui.R
import com.mop.ui.bean.UnSplashPhoto
import com.mop.ui.databinding.FragmentUiSon2Binding
import com.mop.ui.viewmodel.Son2VM
import kotlinx.android.synthetic.main.fragment_ui_son_2.*


class Son2Fragment : BaseDBFragment<Son2VM, FragmentUiSon2Binding>() {

    override fun initView(savedInstanceState: Bundle?) {
        rv.linear().setup {
            addType<UnSplashPhoto>(R.layout.item_banner)
        }
    }

    override fun initData() {
        initNetUnsplash()
        mDatabind.viewmodel = viewModel
        BRV.modelId = BR.viewModel
        viewModel.liveData.observe(this, {
            rv.addModels(it)
//            rv.models = it
        })
        viewModel.queryBanner()
    }
}