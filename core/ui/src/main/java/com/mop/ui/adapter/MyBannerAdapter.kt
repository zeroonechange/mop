package com.mop.ui.adapter

import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mop.base.base.BaseApp
import com.mop.ui.R
import com.mop.ui.bean.UnSplashPhotoItem
import com.mop.ui.databinding.ItemBannerImgBinding
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder


class MyBannerAdapter : BaseBannerAdapter<UnSplashPhotoItem>() {

    override fun bindData(
        holder: BaseViewHolder<UnSplashPhotoItem>?,
        data: UnSplashPhotoItem?,
        position: Int,
        pageSize: Int
    ) {
        val dataBinding: ItemBannerImgBinding? = DataBindingUtil.bind(holder!!.itemView)
        dataBinding?.data = data

        dataBinding?.ivBanner?.let {
            Glide.with(BaseApp.instance!!).load(data?.urls?.regular)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(
                    it
                )
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_banner_img
    }
}