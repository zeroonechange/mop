package com.mop.base.base.act

import android.view.View
import androidx.databinding.ViewDataBinding
import com.mop.base.base.viewmodel.BaseViewModel
import com.mop.base.ext.inflateBindingWithGeneric

abstract class BaseDBAct<DB : ViewDataBinding, VM : BaseViewModel> : BaseVMAct<VM>() {

    override fun layoutID(): Int =0

    lateinit var mDataBinding: DB

    override fun initDataBind(): View? {
        mDataBinding = inflateBindingWithGeneric(layoutInflater)
//        mDataBinding.setVariable(BR._all, viewModel)  这样不太好  需要根据ID来双向绑定
        return mDataBinding.root
    }
}