package com.mop.data

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.mop.base.base.act.BaseDBAct
import com.mop.base.util.ArouterTable
import com.mop.data.databinding.DataActivityMainBinding

@Route(path = ArouterTable.DATA_MAIN_ACT)
class DataMainActivity : BaseDBAct<DataMainVM, DataActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

    }


    override fun initData() {
        mDataBinding.viewModel = viewModel
        val name = intent.getStringExtra("NAME")
        Log.e(TAG, "initData: name=$name")

    }
}