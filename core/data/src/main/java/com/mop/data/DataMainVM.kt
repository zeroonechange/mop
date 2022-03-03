package com.mop.data

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mop.base.data.MyRepository
import com.mop.base.mvvm.BaseViewModel
import kotlinx.coroutines.flow.collect

class DataMainVM(app: Application) : BaseViewModel<MyRepository>(app) {

    var app: Application = app

    init {

    }

    val data = MutableLiveData<List<Any>>()
    val str = MutableLiveData<String>()

    private suspend fun queryBanner() {
        mModel.queryBanner().collect {
            str.postValue(it.data.toString())
            when (it.code == 200) {

            }
        }
    }

    fun loadData() {
        launchUI {
            queryBanner()
        }
    }
}