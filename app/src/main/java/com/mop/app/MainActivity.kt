package com.mop.app

import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.mop.app.databinding.ActivityMainBinding
import com.mop.base.data.config.RouterTable
import com.mop.base.ui.BaseAct
import com.mop.base.utils.statusbar.StatusBarUtils

class MainActivity : BaseAct<ActivityMainBinding>(R.layout.activity_main) {

    override fun initView() {
        //        Utils.setStatusBar(this)
        StatusBarUtils.setLightStatusBar(this, true)
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

    override fun initData() {
        Log.e(TAG, "----> initData: " )
    }
}