package com.mop.app

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.mop.app.viewmodel.SplashVM
import com.mop.app.databinding.ActivityMainBinding
import com.mop.base.base.act.BaseVBAct
import com.mop.base.util.ArouterTable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseVBAct<SplashVM, ActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        //        Utils.setStatusBar(this)
//        StatusBarUtils.setLightStatusBar(this, true)
        tvUI.setOnClickListener {
            ARouter.getInstance()
                .build(ArouterTable.UI_HOME_ACT)
                .greenChannel()
                .navigation()
        }

        tvData.setOnClickListener {
            ARouter.getInstance()
                .build(ArouterTable.DATA_MAIN_ACT)
                .withString("NAME", "OMP")
                .greenChannel()
                .navigation()
        }
    }

    override fun initData() {
        Log.e(TAG, "----> initData: ")
    }
}