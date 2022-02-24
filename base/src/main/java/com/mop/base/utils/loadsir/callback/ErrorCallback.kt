package com.mop.base.utils.loadsir.callback


import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import com.mop.base.R

class ErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.load_sir_callback_error
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }
}