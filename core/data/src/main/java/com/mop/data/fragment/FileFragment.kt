package com.mop.data.fragment

import android.os.Bundle
import com.mop.base.base.fragment.BaseDBFragment
import com.mop.data.databinding.FragmentDataFileBinding
import com.mop.data.viewmodel.FileVM

class FileFragment : BaseDBFragment<FileVM, FragmentDataFileBinding>() {

    override fun initData() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = viewModel
    }
}