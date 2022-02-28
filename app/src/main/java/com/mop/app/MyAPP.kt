package com.mop.app

import com.alibaba.android.arouter.launcher.ARouter
import com.mop.base.AppStateTracker
import com.mop.base.app.BaseApp
import com.mop.base.data.ApiService
import com.mop.base.data.HttpRequest
import com.mop.base.data.TrustCertPathUtils
import com.mop.base.data.config.GlobalConfig
import com.mop.base.utils.LogUtil
import com.mop.base.utils.loadsir.callback.EmptyCallback
import com.mop.base.utils.loadsir.callback.ErrorCallback
import com.mop.base.utils.loadsir.callback.LoadingCallback
import com.mop.base.utils.loadsir.callback.NetErrorCallback

class MyAPP : BaseApp() {

    override fun onCreate() {
        super.onCreate()

        // 设置请求 URL
        HttpRequest.mDefaultBaseUrl = ApiService.SERVER_URL // 初始化 loading加载框架 - Sir
        GlobalConfig.initLoadSir(
            LoadingCallback::class.java,
            EmptyCallback::class.java,
            ErrorCallback::class.java,
            NetErrorCallback::class.java,
        )

        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) { // 打印日志
            ARouter.openLog(); // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        } // 尽可能早，推荐在Application中初始化
        ARouter.init(this)

        // 是否支持点击事件间隔一定时间，可局部设置
        GlobalConfig.Click.gIsClickInterval = true // 设定间隔时间，毫秒为单位，默认是 800 毫秒，可局部设置
        GlobalConfig.Click.gClickIntervalMilliseconds = 700

        // 设置为 true 才可以使用框架的 AppActivityManager 类
        GlobalConfig.gIsNeedActivityManager = true // 开启动态修改 BaseURL 功能，配合 HttpRequest.multiClickToChangeBaseUrl 方法使用
        GlobalConfig.gIsNeedChangeBaseUrl = true // 开启侧滑返回功能，可局部设置，需 activity 的主题的 android:windowIsTranslucent 为 true，即透明
        GlobalConfig.gIsSupportSwipe = false

        // 加载中对话框相关的配置，可局部设置
        GlobalConfig.LoadingDialog.gIsNeedLoadingDialog = true // 是否开启取消对话框后，同步取消耗时任务
        GlobalConfig.LoadingDialog.gIsCancelConsumingTaskWhenLoadingDialogCanceled = true // 对话框是否可按返回键取消
        GlobalConfig.LoadingDialog.gLoadingDialogCancelable = true // 对话框是否可点击外部区域取消
        GlobalConfig.LoadingDialog.gLoadingDialogCanceledOnTouchOutside = true

        //initBugly()

        // 可追踪应用的状态是在前台还是后台，注意：锁屏也是后台
        AppStateTracker.track(object : AppStateTracker.AppStateChangeListener {
            override fun appTurnIntoForeground() {
                LogUtil.i("MyApp", "commonLog - appTurnIntoForeground: ")
            }

            override fun appTurnIntoBackground() {
                LogUtil.i("MyApp", "commonLog - appTurnIntoBackground: ")
            }
        })

        TrustCertPathUtils.initApplication(this)

    }

    override fun onMainProcessInit() {
        super.onMainProcessInit()
    }

    override fun onOtherProcessInit(processName: String) {
        super.onOtherProcessInit(processName)
    }
}