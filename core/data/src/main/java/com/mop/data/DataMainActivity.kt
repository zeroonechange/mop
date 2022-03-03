package com.mop.data

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.mop.base.data.config.RouterTable
import com.mop.base.ui.DataBindingBaseActivity
import com.mop.data.databinding.DataActivityMainBinding

@Route(path = RouterTable.DATA_MAIN_ACT)
class DataMainActivity : DataBindingBaseActivity<DataActivityMainBinding, DataMainVM>(R.layout.data_activity_main, BR.viewModel) {

    private val TAG: String = "DataMainActivity"


    override fun initData() {
        super.initData()

        val name = intent.getStringExtra("NAME")
        Log.e(TAG, "initData: name=$name")

    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.data.observe(this, Observer {
            Log.e(TAG, "initViewModel: ")
        })

        mViewModel.str.observe(this, Observer {
            Log.e(TAG, "initViewModel: $it")
        })
        mViewModel.loadData()
    }

    override fun widgetClick(view: View?) {
        mViewModel.loadData()
    }
}