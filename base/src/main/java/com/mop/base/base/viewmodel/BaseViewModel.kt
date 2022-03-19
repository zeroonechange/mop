package com.mop.base.base.viewmodel

import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {

    companion object{
        val TAG = javaClass.simpleName.toString()
    }

}