package com.mop.ui.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.scopeNetLife
import com.drake.net.Get
import com.mop.base.base.viewmodel.BaseViewModel
import com.mop.ui.adapter.MyBannerAdapter
import com.mop.ui.bean.UnSplashPhoto
import com.mop.ui.bean.UnSplashPhotoItem
import com.zhpan.bannerview.BannerViewPager


class Son2VM() : BaseViewModel() {

    var mList = ArrayList<Any>()
    var liveData = MutableLiveData<List<Any>>()

    fun queryBanner() {
        scopeNetLife {
            val _data = Get<ArrayList<UnSplashPhotoItem>>("photos/random/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA&count=6").await()
            mList.add(UnSplashPhoto(_data))
            liveData.value = mList
        }
    }
}


@BindingAdapter(value = ["bannerData"], requireAll = false)
fun bindMyBanner(
    banner: BannerViewPager<UnSplashPhotoItem>,
    dataList: List<UnSplashPhotoItem>
) {
    val adapter = MyBannerAdapter()
    banner.adapter = adapter
    banner.create(dataList)
}