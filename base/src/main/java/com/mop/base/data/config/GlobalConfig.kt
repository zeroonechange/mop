package com.mop.base.data.config

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.mop.base.R

/**
 * 全局配置
 */
object GlobalConfig {
    /**
     * 是否保存 log 到缓存目录。目录地址：
     * /sdcard/Android/data/应用包名/cache/Log
     *
     * 建议打包提测的都开启保存。比如 beta 构建选项
     */
    var gIsSaveLog = false

    /**
     * activity 是否支持侧滑返回。
     * 若支持，则 theme 必须实现以下属性：
     * <item name="android:windowIsTranslucent">true</item>
     *
     * 并且框架会自动把 windowBackground 设置为透明
     */
    var gIsSupportSwipe = false

    /**
     * 是否需要管理 Activity 堆栈
     */
    var gIsNeedActivityManager = true

    /**
     * 是否需要动态修改 BaseURL，如果需要，请设置为 true，并在合适的位置调用：[com.gwm.libbase.http.HttpRequest.multiClickToChangeBaseUrl]
     */
    var gIsNeedChangeBaseUrl = false


    object Click {

        /**
         * 在 xml 配置点击事件，可配置的属性如下：
         * onClickCommand 点击事件
         * isInterval 是否开启防止点击过快
         * intervalMilliseconds 防止点击过快的间隔时间，毫秒为单位
         *
         * 这里可全局设置是否开启防止点击事件过快的功能，局部可单独开启或关闭。
         *
         * 如果关闭，那么和 setOnClickListener 没啥区别
         */
        var gIsClickInterval = false

        /**
         * 点击事件时间间隔
         */
        var gClickIntervalMilliseconds = 800
    }

    object LoadingDialog {
        /**
         * 加载中对话框是否可点击返回键隐藏对话框
         */
        var gLoadingDialogCancelable = false

        /**
         * 加载中对话框是否可点击对话框内容外的区域隐藏对话框
         */
        var gLoadingDialogCanceledOnTouchOutside = false

        /**
         * 是否在取消对话框的同时，取消耗时任务，只有在上述两个变量 [gLoadingDialogCancelable]，[gLoadingDialogCanceledOnTouchOutside]
         * 有一个设置为 true 的情况下才有效
         */
        var gIsCancelConsumingTaskWhenLoadingDialogCanceled = gLoadingDialogCancelable || gLoadingDialogCanceledOnTouchOutside

        /**
         * 页面在显示数据时需要时间，因此显示加载中对话框，如果不需要，可以设置为 false，以免创建不必要的对象
         */
        var gIsNeedLoadingDialog = true

        /**
         * 加载中对话框的 layout
         */
        @LayoutRes
        var gLoadingDialogLayout = R.layout.mvvm_dlg_loading

        /**
         * 加载中对话框的 layout 消息id
         */
        @IdRes
        var gLoadingDialogLayoutMsgId = R.id.tv_msg
    }

    object AppBar {
        /**
         * 全局标题栏布局配置
         */
        @LayoutRes
        var gAppBarLayoutId: Int? = null
    }
}