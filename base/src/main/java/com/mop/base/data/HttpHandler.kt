package com.mop.base.data

import com.google.gson.JsonSyntaxException
import com.mop.base.mvvm.DataStatus
import com.mop.base.mvvm.IBaseResponse
import com.mop.base.mvvm.ResultResponse
import com.mop.base.utils.LogUtil
import com.mop.base.utils.LoginServiceUtil
import com.mop.base.utils.NetworkUtil

import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeoutException

object HttpHandler {
    /**
     * 处理请求结果
     *
     * [entity] 实体
     * [onSuccess] 状态码对了就回调
     * [onResult] 状态码对了，且实体不是 null 才回调
     * [onFailed] 有错误发生，可能是服务端错误，可能是数据错误，详见 code 错误码和 msg 错误信息
     */
    fun <T> handleResult(
        entity: IBaseResponse<T?>?,
        onSuccess: (() -> Unit)? = null,
        onResult: ((t: T) -> Unit),
        onFailed: ((code: Int, msg: String?) -> Unit)? = null,
        onNoLogin: (() -> Unit)? = null,
        onNoData: (() -> Unit)? = null,
    ) { // 防止实体为 null
        if (entity == null) {
            onFailed?.invoke(entityNullable, msgEntityNullable)
            return
        }
        val code = entity.code()
        val msg = entity.msg() // 防止状态码为 null
        if (code == null) {
            onFailed?.invoke(entityCodeNullable, msgEntityCodeNullable)
            return
        } // 请求成功
        if (entity.isSuccess()) { // 回调成功
            onSuccess?.invoke() // 实体不为 null 才有价值
            entity.data()?.let { onResult.invoke(it) }
            if (entity.data() == null) {
                onNoData?.invoke()
            }
        } else if (entity.code() == 403 || entity.code() == 401) {
            LoginServiceUtil.getService().logout()
            onFailed?.invoke(code, msg)
            onNoLogin?.invoke()
        } else { // 失败了
            onFailed?.invoke(code, msg)
        }
    }

    /**
     * 处理异常
     */
    fun handleException(
        e: Exception, onFailed: (code: Int, msg: String?) -> Unit
    ) {
        if (LogUtil.isLog()) {
            e.printStackTrace()
        }
        return if (e is HttpException) {
            onFailed(
                notHttpException, "$msgNotHttpException"
            )
        } else {
            val log = LogUtil.getStackTraceString(e)
            if (LogUtil.isLog()) {
                LogUtil.e("TAG", log)
            }

            onFailed(
                notHttpException, "$msgNotHttpException"
            )
        }
    }


    /**
     * 处理异常(目前flow使用)
     */
    fun handleException(
        e: Throwable,
    ): ResultResponse<*> {
        if (LogUtil.isLog()) {
            e.printStackTrace()
        }
        var result = ResultResponse("", entityNullable, "", false, DataStatus.ERROR)

        if (e is UnknownHostException) {
            if (!NetworkUtil.isConnected()) {
                result.code = unNetWorkException
                result.message = SocketTimeoutException
            } else {
                result.code = notHttpException
                result.message = msgNotHttpException
            }
        } else if (e is HttpException) {
            result.code = e.code()
            result.message = e.message()
        } else if (e is SocketTimeoutException || e is TimeoutException) {
            result.code = notHttpException
            result.message = SocketTimeoutException
        } else if (e is ConnectException) {
            result.code = notHttpException
            result.message = networkWeakTryAgain
        } else if (e is JsonSyntaxException || e is JSONException || e is ParseException) {
            result.code = parseError
            result.message = dataParseTryAgain
        } else {
            result.code = unKnown
            result.message = e.message
        }
        return result
    }
}