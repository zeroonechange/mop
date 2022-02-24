package com.mop.app

import android.util.Log
import androidx.lifecycle.Observer
import com.mop.app.data.MainViewModel
import com.mop.app.databinding.ActivityMainBinding
import com.mop.base.ui.DataBindingBaseActivity

class MainActivity : DataBindingBaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main, BR.viewModel) {
    private val TAG: String = "MainActivity"

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.onCreate(this)

        mViewModel.data.observe(this, Observer {
            Log.e(TAG, "initViewModel: ")
        })
        mViewModel.loadData()
    }

}