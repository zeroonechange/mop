package com.mop.base.mvvm

import android.widget.Toast
import com.mop.base.data.config.AppConstants
import com.mop.base.data.config.LiveBusMsgDefine
import com.mop.base.data.entityCodeNullable
import com.mop.base.data.msgEntityCodeNullable
import com.mop.base.utils.LoginServiceUtil
import com.mop.base.utils.bus.LiveBusStick
import com.mop.base.utils.bus.LiveDataBus

open class ResultResponse<T>(
    var data: T?,
    var code: Int?,
    var message: String?,
    var success : Boolean?,
    var status: DataStatus?
) : IBaseResponse<T> {
    override fun code() = if (success == true) 0 else code

    override fun msg() = message

    override fun data() = data

    override fun isSuccess() = if (success == true) true else code == 0

    fun onResult(
        onSuccess: ((data: T?) -> Unit)? = null,
        onError: ((code: Int, message: String?) -> Unit)? = null
    ) {
        // 防止状态码为 null
        if (null == code) {
            code = entityCodeNullable
            message = msgEntityCodeNullable
            onError?.let { it(entityCodeNullable, msgEntityCodeNullable) }
            return
        }
        when {
            isSuccess() -> {
                status = DataStatus.SUCCESS
                onSuccess?.invoke(data)
            }
            code() == 403 -> {
                LoginServiceUtil.getService().clearLoginData()
                Toast.makeText(AppConstants.context, "账号异地登录！请检查账号安全，及时修改密码！", Toast.LENGTH_SHORT).show()
                status = DataStatus.ERROR
                LoginServiceUtil.goToLogin()
                LiveDataBus.send("loginout", "")
                LiveBusStick.get().postEvent(LiveBusMsgDefine.LOGIN_STATUS, LiveBusMsgDefine.LOGIN_OUT)
            }
            code() == 401 -> { //token失效，跳转到登录界面
                status = DataStatus.ERROR
                LoginServiceUtil.getService().clearLoginData()
                Toast.makeText(AppConstants.context, "认证过期，需要重新登录！", Toast.LENGTH_SHORT).show()
                LoginServiceUtil.goToLogin()
                LiveDataBus.send("loginout", "")
                LiveBusStick.get().postEvent(LiveBusMsgDefine.LOGIN_STATUS, LiveBusMsgDefine.LOGIN_OUT)
            }
            code() == 40001 -> {  //此处只有登陆时用到
                //onBind(entity.data())
            }
            else -> { //网络请求失败
                status = DataStatus.ERROR
                onError?.invoke(code!!, message)
            }
        }
    }
}

sealed class DataStatus {
    object SUCCESS : DataStatus()
    object ERROR : DataStatus()
}