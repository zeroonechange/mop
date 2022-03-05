package com.mop.ui.viewmodel

import android.app.Application
import android.view.View
import android.widget.Toast
import com.mop.base.data.MyRepository
import com.mop.base.mvvm.BaseViewModel

class UIMainVM(app: Application) : BaseViewModel<MyRepository>(app) {

    var searchMe = View.OnClickListener {
        Toast.makeText(app, "没啥好搜索的", Toast.LENGTH_LONG).show()
    }

    var checkMsg = View.OnClickListener {
        Toast.makeText(app, "没有消息哦", Toast.LENGTH_LONG).show()
    }

}