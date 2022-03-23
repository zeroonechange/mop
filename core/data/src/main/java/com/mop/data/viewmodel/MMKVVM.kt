package com.mop.data.viewmodel

import android.util.Log
import android.view.View
import com.mop.base.base.BaseApp
import com.mop.base.base.viewmodel.BaseViewModel
import com.mop.data.utils.CacheUtil
import com.tencent.mmkv.MMKV


class MMKVVM : BaseViewModel() {

    val init = View.OnClickListener {
       val path = BaseApp.instance!!.filesDir.absolutePath+ "/mmkv"
        Log.e(TAG, " path = $path", )
        MMKV.initialize(path)
    }

    companion object{
        const val KEY_STRING="aaa1"
        const val KEY_INT ="aaa2"
        const val KEY_FLOAT ="aaa3"
        const val KEY_BOOL ="aaa4"
    }

    val query = View.OnClickListener {
        val aaa1 = CacheUtil.getString(KEY_STRING)
        val aaa2 = CacheUtil.getInt(KEY_INT)
        val aaa3 = CacheUtil.getFloat(KEY_FLOAT)
        val aaa4 = CacheUtil.getBool(KEY_BOOL)
        Log.e(TAG, "query====> aaa1=$aaa1, aaa2=$aaa2, aaa3=$aaa3, aaa4=$aaa4 ", )
    }

    val update = View.OnClickListener {
        val time = System.currentTimeMillis()
        val s = time.toString().last().toInt()
        val aaa1 = CacheUtil.savaString(KEY_STRING, "t=$time")
        val aaa2 = CacheUtil.saveInt(KEY_INT, s)
        val aaa3 =CacheUtil.saveFloat(KEY_FLOAT, (s+ 23.212222).toFloat())
        val aaa4 =CacheUtil.saveBool(KEY_BOOL, (s%2==0))
        Log.e(TAG, "update====> aaa1=$aaa1, aaa2=$aaa2, aaa3=$aaa3, aaa4=$aaa4 ", )
    }

    val delete = View.OnClickListener {
        val kv = MMKV.mmkvWithID("mop")
        kv.encode("user", "")
    }
}