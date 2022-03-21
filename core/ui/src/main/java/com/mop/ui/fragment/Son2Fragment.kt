package com.mop.ui.fragment

import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.*
import com.mop.base.base.BaseApp
import com.mop.base.base.fragment.BaseDBFragment
import com.mop.base.net.initNetUnsplash
import com.mop.ui.BR
import com.mop.ui.R
import com.mop.ui.bean.SplashData
import com.mop.ui.bean.SplashImg
import com.mop.ui.bean.UnSplashPhoto
import com.mop.ui.databinding.FragmentUiSon2Binding
import com.mop.ui.viewmodel.Son2VM
import kotlinx.android.synthetic.main.fragment_ui_son_2.*


class Son2Fragment : BaseDBFragment<Son2VM, FragmentUiSon2Binding>() {

    private var bindingAdapter: BindingAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        BRV.modelId = BR.viewModel
        rv.linear().setup {
            addType<UnSplashPhoto>(R.layout.item_banner)
            addType<SplashData>(R.layout.item_straggle)
            onCreate {
                if (it == R.layout.item_straggle) {
                    findView<RecyclerView>(R.id.rv).staggered(2, RecyclerView.VERTICAL).divider {
                        setDrawable(R.drawable.divider_horizontal)
                        startVisible = true
                        endVisible = true
                    }.setup {
                        addType<SplashImg>(R.layout.item_img)

                        onBind {
                            val layoutParams = itemView.layoutParams   // 设置动态高度
                            layoutParams.height = getModel<SplashImg>().height
                            itemView.layoutParams = layoutParams
                            val iv = findView<ImageView>(R.id.iv)
                            val url = getModel<SplashImg>().url

                            Glide
                                .with(this@Son2Fragment)
                                .load(url)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(iv)

                        }
                    }
                }
            }
            onBind {
                if (itemViewType == R.layout.item_straggle) {
                    findView<RecyclerView>(R.id.rv).models = getModel<SplashData>().data
                }
            }
        }
        bindingAdapter = rv.bindingAdapter
    }

    override fun initData() {
        initNetUnsplash()
        mDatabind.viewmodel = viewModel
        viewModel.liveData.observe(this, { //            rv.addModels(arrayListOf(it))
            //            rv.models = it
            bindingAdapter?.clearHeader()
            viewModel.listHeaderData.forEach {
                bindingAdapter?.addHeader(it.value, it.key, false)
            }
        })
        viewModel.queryBanner()
        viewModel.queryHotGirl()
    }
}