package com.mop.base.base.act

import android.view.View
import androidx.viewbinding.ViewBinding
import com.mop.base.base.viewmodel.BaseViewModel
import com.mop.base.ext.inflateBindingWithGeneric

abstract class BaseVBAct<VB : ViewBinding, VM : BaseViewModel> : BaseVMAct<VM>() {

    override fun layoutID(): Int = 0

    lateinit var mViewBind: VB

    override fun initDataBind(): View? {
        mViewBind = inflateBindingWithGeneric(layoutInflater)
        return mViewBind.root
    }
}