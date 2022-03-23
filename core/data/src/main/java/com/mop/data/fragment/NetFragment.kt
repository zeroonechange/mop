package com.mop.data.fragment

import android.os.Bundle
import com.mop.base.base.fragment.BaseDBFragment
import com.mop.base.net.initNetUnsplash
import com.mop.data.databinding.FragmentDataNetBinding
import com.mop.data.viewmodel.NetVM

class NetFragment : BaseDBFragment<NetVM, FragmentDataNetBinding>() {

    override fun initData() {
        initNetUnsplash()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = viewModel
    }
}