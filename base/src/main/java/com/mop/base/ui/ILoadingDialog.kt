package com.mop.base.ui

import android.app.Dialog
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.mop.base.data.config.GlobalConfig

/**
 * 加载中对话框接口
 */
interface ILoadingDialog {
    /**
     * 显示加载中对话框
     */
    fun showLoadingDialog(dialog: Dialog, msg: String?) { //此处原本读取GlobalConfig 里的配置 但是不生效，只有写死生效 ，可能是其他地方改了 GlobalConfig
        if (dialog.isShowing) {
            return
        }

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show() // 只有在允许取消对话框和同时允许取消任务时，才有必要设置监听
        if (isCancelConsumingTaskWhenLoadingDialogCanceled() && (isLoadingDialogCancelable() || isLoadingDialogCanceledOnTouchOutside())) {
            dialog.setOnCancelListener { onCancelLoadingDialog() }
        }
        dialog.findViewById<TextView>(loadingDialogLayoutMsgId())?.text = msg
    }

    /**
     * 加载中对话框被用户手动取消了，则回调此方法
     */
    fun onCancelLoadingDialog()

    /**
     * 隐藏加载中对话框
     */
    fun dismissLoadingDialog(dialog: Dialog) = dialog.dismiss()

    @LayoutRes
    fun loadingDialogLayout() = GlobalConfig.LoadingDialog.gLoadingDialogLayout

    @IdRes
    fun loadingDialogLayoutMsgId() = GlobalConfig.LoadingDialog.gLoadingDialogLayoutMsgId

    fun isNeedLoadingDialog() = GlobalConfig.LoadingDialog.gIsNeedLoadingDialog

    fun isLoadingDialogCancelable() = GlobalConfig.LoadingDialog.gLoadingDialogCancelable

    fun isLoadingDialogCanceledOnTouchOutside() = GlobalConfig.LoadingDialog.gLoadingDialogCanceledOnTouchOutside


    fun isCancelConsumingTaskWhenLoadingDialogCanceled() = GlobalConfig.LoadingDialog.gIsCancelConsumingTaskWhenLoadingDialogCanceled
}