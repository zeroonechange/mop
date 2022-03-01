package com.mop.app

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.mop.app.data.MainVM
import com.mop.app.databinding.ActivityMainBinding
import com.mop.base.ui.DataBindingBaseActivity

class MainActivity : DataBindingBaseActivity<ActivityMainBinding, MainVM>(R.layout.activity_main, BR.viewModel) {
    private val TAG: String = "MainActivity"

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