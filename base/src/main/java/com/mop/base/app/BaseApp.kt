package com.mop.base.app

import android.app.Application

open class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initApp(this)
    }

    /**
     * 通常来说应用只有一个进程，进程名称是当前的包名，你需要针对这个进程做一些初始化。
     * 如果你引入了第三方服务，比如地图，推送什么的，很可能对方是开了个额外的进程在跑的，
     * 这个时候就没必要初始化你的资源了，因为它根本用不上你的。
     *
     * 如果你自己开了多进程，那么就复写 [onCreate] 自己判断要怎么初始化资源吧
     */
    open fun onMainProcessInit() {}

    /**
     * 其他进程初始化，[processName] 进程名
     */
    open fun onOtherProcessInit(processName: String) {}

    companion object {
        private lateinit var app: Application

        @JvmStatic
        fun initApp(app: Application) {
            Companion.app = app

//            TCA.initSDK(app)
            //扑捉崩溃异常（开发模式方便定位报错点 。后续上线需要打开）
            // LogUtil.init()
        }

        @JvmStatic
        fun getInstance(): Application {
            return app
        }
    }
}