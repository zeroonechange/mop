package com.mop.base.data

import com.mop.base.mvvm.BaseModel
import com.mop.base.utils.LoginServiceUtil


abstract class BaseRepository : BaseModel() {

    var token = LoginServiceUtil.getService().token

    init {
        token = LoginServiceUtil.getService().token
    }

    fun initData() {
        token = LoginServiceUtil.getService().token
    }
}