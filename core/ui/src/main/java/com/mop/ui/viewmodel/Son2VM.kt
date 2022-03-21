package com.mop.ui.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.scopeNetLife
import com.drake.net.Get
import com.mop.base.base.BaseApp
import com.mop.base.base.viewmodel.BaseViewModel
import com.mop.base.ext.screenWidth
import com.mop.ui.adapter.MyBannerAdapter
import com.mop.ui.bean.*
import com.zhpan.bannerview.BannerViewPager


class Son2VM() : BaseViewModel() {

    val listHeaderData = HashMap<Int, Any>()
    var liveData = MutableLiveData<Any>()

    fun queryBanner() {
        scopeNetLife {
            val _data = Get<ArrayList<UnSplashPhotoItem>>("photos/random/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA&count=6").await()
            val bannerData = UnSplashPhoto(_data)
            listHeaderData[0] = bannerData
            liveData.postValue(bannerData)
        }
    }


    fun queryHotGirl() {
        scopeNetLife {
            val _data = Get<HotGirl>("search/photos/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA&query=\"hot girl\"&page=1&per_page=10").await()
            var imgList = arrayListOf<SplashImg>()
            val w1 = BaseApp.instance!!.screenWidth / 2
            _data.results.forEach {
                imgList.add(SplashImg(it.urls.regular, w1 * it.height / it.width))
            }
            val allData = SplashData(imgList)
            listHeaderData[1] = allData
            liveData.postValue(allData)
        }
    }
}


@BindingAdapter(value = ["bannerData"], requireAll = false)
fun bindMyBanner(
    banner: BannerViewPager<UnSplashPhotoItem>, dataList: List<UnSplashPhotoItem>?
) {
    val adapter = MyBannerAdapter()
    banner.adapter = adapter
    banner.create(dataList)
}