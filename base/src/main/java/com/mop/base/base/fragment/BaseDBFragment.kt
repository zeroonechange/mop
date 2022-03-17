package com.mop.base.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.mop.base.base.viewmodel.BaseViewModel
import com.mop.base.ext.inflateBindingWithGeneric

abstract class BaseDBFragment<DB : ViewDataBinding, VM : BaseViewModel> : BaseVMFragment<VM>() {

    override fun layoutID(): Int = 0

    private var _binding: DB? = null
    val mDatabind: DB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBindingWithGeneric(inflater, container, false)
        return mDatabind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}