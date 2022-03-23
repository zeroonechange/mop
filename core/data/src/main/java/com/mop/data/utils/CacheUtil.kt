package com.mop.data.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.mop.data.bean.UserInfo
import com.tencent.mmkv.MMKV

object CacheUtil {

    const val ID = "mop_v1"
    const val USER = "user"
    const val LOGIN = "login"
    const val FIRST_LOGIN = "first_login"


    private fun getMMKV(): MMKV {
        return MMKV.mmkvWithID(ID)
    }

    fun savaString(key: String, value: String): Boolean {
        return getMMKV().encode(key, value)
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return getMMKV().decodeString(key, defaultValue)
    }

    fun saveInt(key: String, value: Int): Boolean {
        return getMMKV().encode(key, value)
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return getMMKV().getInt(key, defaultValue)
    }


    fun saveBool(key: String, value: Boolean): Boolean {
        return getMMKV().encode(key, value)
    }

    fun getBool(key: String, defaultValue: Boolean = false): Boolean {
        return getMMKV().decodeBool(key, defaultValue)
    }

    fun saveFloat(key: String, value: Float): Boolean {
        return getMMKV().encode(key, value)
    }

    fun getFloat(key: String, defaultValue: Float = 0.0f): Float {
        return getMMKV().decodeFloat(key, defaultValue)
    }


    fun getUser(): UserInfo? {
        val kv = getMMKV()
        val userStr = kv.decodeString(USER)
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, UserInfo::class.java)
        }
    }

    fun setUser(userResponse: UserInfo?) {
        val kv = getMMKV()
        if (userResponse == null) {
            kv.encode(USER, "")
            setIsLogin(false)
        } else {
            kv.encode(USER, Gson().toJson(userResponse))
            setIsLogin(true)
        }
    }

    fun isLogin(): Boolean {
        val kv = getMMKV()
        return kv.decodeBool(LOGIN, false)
    }

    fun setIsLogin(isLogin: Boolean) {
        val kv = getMMKV()
        kv.encode(LOGIN, isLogin)
    }


    fun isFirst(): Boolean {
        val kv = getMMKV()
        return kv.decodeBool(FIRST_LOGIN, true)
    }


    fun setFirst(first: Boolean): Boolean {
        val kv = getMMKV()
        return kv.encode(FIRST_LOGIN, first)
    }
}