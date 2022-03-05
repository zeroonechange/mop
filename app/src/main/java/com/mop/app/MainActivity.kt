package com.mop.app

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.mop.app.data.SplashVM
import com.mop.app.databinding.ActivityMainBinding
import com.mop.base.data.config.RouterTable
import com.mop.base.ui.DataBindingBaseActivity
import com.mop.base.utils.statusbar.StatusBarUtils

class MainActivity :
    DataBindingBaseActivity<ActivityMainBinding, SplashVM>(R.layout.activity_main, BR.viewModel) {

    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Utils.setStatusBar(this)
        StatusBarUtils.setLightStatusBar(this, true)
    }

    override fun initListener() {
        super.initListener()

        mBinding.tvUI.setOnClickListener {
            ARouter.getInstance()
                .build(RouterTable.UI_HOME_ACT)
                .greenChannel()
                .navigation()
        }

        mBinding.tvData.setOnClickListener {
            ARouter.getInstance()
                .build(RouterTable.DATA_MAIN_ACT)
                .withString("NAME", "OMP")
                .greenChannel()
                .navigation()
        }
    }
}