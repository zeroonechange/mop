package com.mop.base.utils


internal object CheckUtil {

    fun checkStartAndFinishEvent(event: Any?) {
        if (event == null) {
            throw RuntimeException(
                "GlobalConfig.StartAndFinish.gIsViewModelNeedStartAndFinish 变量可全局开启或关闭 ViewModel 的 startActivity/finish 等方法，而 Activity/Fragment 的 isViewModelNeedStartAndFinish() 方法可局部开启/关闭。请查看这两者其中之一是否设置了关闭？"
            )
        }
    }

    fun checkStartForResultEvent(event: Any?) {
        if (event == null) {
            throw RuntimeException(
                "GlobalConfig.StartAndFinish.gIsViewModelNeedStartForResult 变量可全局开启或关闭 ViewModel 的 startActivityForResult 等方法，而 Activity/Fragment 的 isViewModelNeedStartForResult() 方法可局部开启/关闭。请查看这两者其中之一是否设置了关闭？"
            )
        }
    }

    fun checkLoadSirEvent(event: Any?) {
        if (event == null) {
            "必须复写 Activity/Fragment 的 getLoadSirTarget() 方法，且必须调用 GlobalConfig 的 initLoadSir 方法"
        }
    }

    fun checkLoadingDialogEvent(event: Any?) {
        if (event == null) {
            throw RuntimeException(
                "GlobalConfig.LoadingDialog.gIsNeedLoadingDialog 变量可全局开启或关闭 ViewModel 的 showLoadingDialog/dismissLoadingDialog 等方法，而 Activity/Fragment 的 isNeedLoadingDialog() 方法可局部开启/关闭。请查看这两者其中之一是否设置了关闭？"
            )
        }
    }
}