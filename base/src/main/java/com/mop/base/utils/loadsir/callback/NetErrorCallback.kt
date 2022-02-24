package com.mop.base.utils.loadsir.callback


import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.View
import android.widget.Button
import com.kingja.loadsir.callback.Callback
import com.mop.base.R

class NetErrorCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.load_sir_callback_net_error
    }

    override fun onAttach(context: Context?, view: View?) {
        view?.findViewById<Button>(R.id.error_btn)?.setOnClickListener {
            context?.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        }
    }

}