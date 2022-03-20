package com.mop.data

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.drake.net.NetConfig
import com.mop.base.base.act.BaseDBAct
import com.mop.base.util.ArouterTable
import com.mop.data.databinding.DataActivityMainBinding
import java.util.concurrent.TimeUnit

@Route(path = ArouterTable.DATA_MAIN_ACT)
class DataMainActivity : BaseDBAct<DataMainVM, DataActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {
        mDataBinding.viewModel = viewModel
        val name = intent.getStringExtra("NAME")
        Log.e(TAG, "initData: name=$name")
        initNetCongig()
    }

    // https://api.unsplash.com/photos/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA
    // https://api.unsplash.com/photos/random/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA&count=10
    // https://api.unsplash.com/search/photos/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA&query="hot girl"&page=1&per_page=10

    private fun initNetCongig() {
        NetConfig.init("https://api.unsplash.com/") {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
//            addInterceptor()
//            cache()
        }
    }
}