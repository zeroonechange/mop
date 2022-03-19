package com.mop.ui.viewmodel

import android.view.View
import android.widget.Toast
import com.mop.base.base.BaseApp
import com.mop.base.base.viewmodel.BaseViewModel

class UIMainVM : BaseViewModel() {

    var searchMe = View.OnClickListener {
        Toast.makeText(BaseApp.instance, "没啥好搜索的", Toast.LENGTH_LONG).show()
    }

    var checkMsg = View.OnClickListener {
        Toast.makeText(BaseApp.instance, "没有消息哦", Toast.LENGTH_LONG).show()
    }
}