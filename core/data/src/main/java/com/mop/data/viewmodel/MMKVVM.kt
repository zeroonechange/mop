package com.mop.data.viewmodel

import android.util.Log
import android.view.View
import com.mop.base.base.BaseApp
import com.mop.base.base.viewmodel.BaseViewModel
import com.tencent.mmkv.MMKV


class MMKVVM : BaseViewModel() {

    val init = View.OnClickListener {
       val path = BaseApp.instance!!.filesDir.absolutePath+ "/mmkv"
        Log.e(TAG, " path = $path", )
        MMKV.initialize(path)
    }

    val query = View.OnClickListener {
        val kv = MMKV.mmkvWithID("mop")
        val userStr = kv.decodeString("user")
        Log.e(TAG, " $userStr", )
    }

    val update = View.OnClickListener {
        val kv = MMKV.mmkvWithID("mop")
        kv.encode("user", "what time is ${System.currentTimeMillis()}")
    }

    val delete = View.OnClickListener {
        val kv = MMKV.mmkvWithID("mop")
        kv.encode("user", "")
    }
}