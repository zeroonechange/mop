package com.mop.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.drake.brv.utils.BRV
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.mop.base.util.ArouterTable
import kotlinx.android.synthetic.main.activity_ui_nav.*

@Route(path = ArouterTable.UI_MAIN_ACT)
class UINavAct : AppCompatActivity() {

    private inline fun generateData(): ArrayList<String> {
        val data = arrayListOf<String>()
        for (i in 1..50) {
            data.add("data $i")
        }
        return data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_nav)

        BRV.modelId = BR.viewModel
        rv.linear().setup {
            addType<String>(R.layout.item_string)
        }.models = generateData()
    }
}