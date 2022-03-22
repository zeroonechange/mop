package com.mop.data.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.scopeNetLife
import com.drake.net.Get
import com.mop.base.base.viewmodel.BaseViewModel


class NetVM : BaseViewModel() {

    val single = View.OnClickListener {
        Log.e(TAG, "single nothing ???   ")
        scopeNetLife {
            val data =
                Get<String>("photos/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA").await()
            Log.e(TAG, "single 1 :  $data")
        }

        scopeNetLife {
            val data =
                Get<String>("photos/random/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA&count=10")
            Log.e(TAG, "single2 :  $data")
        }
    }

    val queue = View.OnClickListener {
        Log.e(TAG, "queue nothing ???  ")
        scopeNetLife {
            val data = Get<String>("http://0000www.baidu.com/").await() // 请求A 发起GET请求并返回数据
            Log.e(TAG, "a-> $data")
            val datb = Get<String>("http://www.baidu.com/").await() // 请求B 将等待A请求完毕后发起GET请求并返回数据
            Log.e(TAG, "b-> $datb ")
        }
    }

    val async = View.OnClickListener {
        Log.e(TAG, "async nothing ???  ")
        scopeNetLife {
            // 以下两个网络请求属于同时进行中
            val aDeferred = Get<String>("banner/json") // 发起GET请求并返回一个对象(Deferred)表示"任务A"
            val bDeferred =
                Get<String>("photos/random/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA&count=10") // 发起请求并返回"任务B"

            // 随任务同时进行, 但是数据依然可以按序返回
            val aData = aDeferred.await() // 等待任务A返回数据
            val bData = bDeferred.await() // 等待任务B返回数据
            val re = aData + bData
            Log.e(TAG, "re === $re ")
        }
    }
}