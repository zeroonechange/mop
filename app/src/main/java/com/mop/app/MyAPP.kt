package com.mop.app

import com.alibaba.android.arouter.launcher.ARouter
import com.mop.base.base.BaseApp


class MyAPP : BaseApp() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }
}