package com.mop.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationBarView
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.mop.base.base.act.BaseVBAct
import com.mop.base.ext.dp2px
import com.mop.base.util.ArouterTable
import com.mop.base.util.bus.LiveDataBus
import com.mop.ui.databinding.ActivityHomeBinding
import com.mop.ui.fragment.HomeDrawerFragment
import com.mop.ui.fragment.UIMainFragment
import com.mop.ui.fragment.UIMyFragment
import com.mop.ui.utils.FragmentUtil
import com.mop.ui.viewmodel.EmptyVM
import kotlinx.android.synthetic.main.activity_home.*

@Route(path = ArouterTable.UI_HOME_ACT)
class HomeAct : BaseVBAct<EmptyVM, ActivityHomeBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

        navView.isItemHorizontalTranslationEnabled = true
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navView.selectedItemId = R.id.menu_main
        navView.itemIconTintList = null

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        FragmentUtil.showFragment(
            supportFragmentManager,
            R.id.flDrawer,
            HomeDrawerFragment::class.java,
            null
        )

        FragmentUtil.showFragment(
            supportFragmentManager,
            R.id.flContent,
            UIMainFragment::class.java,
            null
        )

        FragmentUtil.preLoad(
            supportFragmentManager,
            R.id.flContent,
            UIMainFragment::class.java,
            null
        )

        floatDialog()

        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_main -> {
                    FragmentUtil.showFragment(
                        supportFragmentManager,
                        R.id.flContent,
                        UIMainFragment::class.java,
                        null
                    )
                }
                R.id.menu_project -> {
                    LiveDataBus.send("openDrawer", 0)
                }
                R.id.menu_system -> {

                }
                R.id.menu_public -> {

                }
                R.id.menu_me -> {
                    FragmentUtil.showFragment(
                        supportFragmentManager,
                        R.id.flContent,
                        UIMyFragment::class.java,
                        null
                    )
                }
            }
            true
        }

        LiveDataBus.observe(this, "openDrawer", Observer { result ->
            if (result == 0) {  // 开
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.openDrawer(Gravity.LEFT)
                }
            } else {  // 关
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT)
                }
            }
        })
    }

    override fun initData() {

    }

    private fun floatDialog() {
        EasyFloat.with(this).setLayout(R.layout.float_dialog)
            .setShowPattern(ShowPattern.CURRENT_ACTIVITY)
            .setTag("myFloatDialog")
            .setDragEnable(true)
            .setGravity(
                Gravity.END or Gravity.BOTTOM,
                dp2px(-5f),
                dp2px(-113f),
            ).registerCallback {
                createResult { b, s, view ->
                    val iv = view?.findViewById<ImageView>(R.id.float_image)
                    val fv = view?.findViewById<View>(R.id.float_v)
                    fv?.setOnClickListener {
                        EasyFloat.dismiss("myFloatDialog")
                    }
                    iv?.also {
                        Glide.with(this@HomeAct)
                            .load("https://zeroonechange.github.io/img/ava2.jpg")
                            .apply(
                                RequestOptions.bitmapTransform(
                                    MultiTransformation(
                                        CenterCrop(),
                                        RoundedCorners(dp2px(14f))
                                    )
                                )
                            )
                            .into(it)
                    }
                }

                touchEvent { view, motionEvent ->
                    view.setOnClickListener {
                        Toast.makeText(this@HomeAct, "点我干啥子", Toast.LENGTH_LONG).show()
                    }
                }
            }.show()
    }


    private fun findImageView(view: View): ImageView? {
        if (view is ViewGroup) {
            view.forEach {
                if (it is ImageView) {
                    return it
                }
            }
        }
        return null
    }
}
