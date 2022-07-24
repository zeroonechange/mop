package com.mop.base.util

import android.app.Activity
import android.os.Bundle
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import java.io.Serializable


class CommonRouterManger private constructor() {

    companion object {
        private var instance: CommonRouterManger? = null
            get() {
                if (field == null) {
                    field = CommonRouterManger()
                }
                return field
            }

        fun get(): CommonRouterManger {
            return instance!!
        }
    }


    fun navigationActivity(path: String) {
        ARouter.getInstance().build(path).navigation()
    }

    fun navigationActivity(
        path: String,
        context: Activity,
        requestCode: Int,
        vararg params: Pair<String, Any>
    ) {
        ARouter.getInstance().build(path).putExtras(*params).navigation(context, requestCode)
    }

    fun navigationActivity(path: String, bundle: Bundle) {
        ARouter.getInstance().build(path)
            .withBundle(CommonConstant.BUNDLE_INFO, bundle).navigation()
    }

    fun navigationActivityParams(path: String, vararg params: Pair<String, Any>) {
        val navigation = ARouter.getInstance().build(path)
            .putExtras(*params)
        navigation.navigation()
    }

    fun Postcard.putExtras(vararg params: Pair<String, Any>): Postcard {
        if (params.isEmpty()) return this
        params.forEach { (key, value) ->
            when (value) {
                is Int -> withInt(key, value)
                is String -> withString(key, value)
                is Boolean -> withBoolean(key, value)
                is Char -> withChar(key, value)
                is Byte -> withByte(key, value)
                is Float -> withFloat(key, value)
                is Bundle -> withBundle(key, value)
                is Double -> withDouble(key, value)
                is ByteArray -> withByteArray(key, value)
                is Serializable -> withSerializable(key, value)
                is ArrayList<*> -> {
                    withStringArrayList(key, value as ArrayList<String>)
                }
                is Object -> withObject(key, value)
            }
        }
        return this
    }
}