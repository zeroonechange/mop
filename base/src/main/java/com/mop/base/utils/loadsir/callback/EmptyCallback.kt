package com.mop.base.utils.loadsir.callback

import com.kingja.loadsir.callback.Callback
import com.mop.base.R

class EmptyCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.load_sir_callback_empty
    }

}