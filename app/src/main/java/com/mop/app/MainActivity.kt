package com.mop.app

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.mop.app.data.MainVM
import com.mop.app.databinding.ActivityMainBinding
import com.mop.base.ui.DataBindingBaseActivity
import com.mop.base.utils.Utils
import com.mop.base.utils.statusbar.StatusBarUtils

class MainActivity : DataBindingBaseActivity<ActivityMainBinding, MainVM>(R.layout.activity_main, BR.viewModel) {
    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Utils.setStatusBar(this)
        StatusBarUtils.setLightStatusBar(this, true)
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