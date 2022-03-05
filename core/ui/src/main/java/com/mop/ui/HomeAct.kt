package com.mop.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.mop.base.data.config.RouterTable
import com.mop.base.mvvm.EmptyVM
import com.mop.base.ui.DataBindingBaseActivity
import com.mop.base.utils.statusbar.StatusBarUtils
import com.mop.ui.databinding.ActivityHomeBinding

@Route(path = RouterTable.UI_HOME_ACT)
class HomeAct :
    DataBindingBaseActivity<ActivityHomeBinding, EmptyVM>(R.layout.activity_home, BR._all) {

    private val TAG: String = "HomeAct"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setLightStatusBar(this, true)
    }
}
