package com.mop.base.mvvm

import android.app.Application
import com.mop.base.data.MyRepository
import com.mop.base.mvvm.BaseViewModel

class EmptyVM(app: Application) : BaseViewModel<MyRepository>(app) {

}