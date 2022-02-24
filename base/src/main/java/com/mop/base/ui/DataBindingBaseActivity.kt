package com.mop.base.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mop.base.mvvm.BaseModel
import com.mop.base.mvvm.BaseViewModel

abstract class DataBindingBaseActivity<V : ViewDataBinding, VM : BaseViewModel<out BaseModel>>(
    @LayoutRes private val layoutId: Int, private val varViewModelId: Int? = null
) : ViewBindingBaseActivity<V, VM>(), View.OnClickListener {

    final override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V = DataBindingUtil.inflate(inflater, layoutId, container, false)

    @CallSuper
    override fun initViewModel() {
        super.initViewModel() // 绑定 v 和 vm
        if (varViewModelId != null) {
            mBinding.setVariable(varViewModelId, mViewModel)
        }

        // 让 LiveData 和 xml 可以双向绑定
        mBinding.lifecycleOwner = this
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

    override fun onClick(view: View?) {
        widgetClick(view)
    }

    open fun widgetClick(view: View?) { //重写可以获取点击事件

    }
}