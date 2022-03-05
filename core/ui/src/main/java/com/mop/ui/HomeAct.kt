package com.mop.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.navigation.NavigationBarView
import com.mop.base.data.config.RouterTable
import com.mop.base.mvvm.EmptyVM
import com.mop.base.ui.DataBindingBaseActivity
import com.mop.base.utils.bus.LiveDataBus
import com.mop.base.utils.statusbar.StatusBarUtils
import com.mop.ui.databinding.ActivityHomeBinding
import com.mop.ui.utils.FragmentUtil
import com.mop.ui.utils.HomeDrawerFragment
import com.mop.ui.utils.UIMainFragment
import com.mop.ui.utils.UIMyFragment

@Route(path = RouterTable.UI_HOME_ACT)
class HomeAct :
    DataBindingBaseActivity<ActivityHomeBinding, EmptyVM>(R.layout.activity_home, BR.viewModel) {

    private val TAG: String = "HomeAct"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setLightStatusBar(this, true)
    }

    override fun initData() {
        super.initData()

        mBinding.navView.isItemHorizontalTranslationEnabled = true
        mBinding.navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        mBinding.navView.selectedItemId = R.id.menu_main
        mBinding.navView.itemIconTintList = null

        mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        FragmentUtil.showFragment(
            supportFragmentManager,
            R.id.fl_drawer,
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


        mBinding.navView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_main->{
                    FragmentUtil.showFragment(
                        supportFragmentManager,
                        R.id.flContent,
                        UIMainFragment::class.java,
                        null
                    )
                }
                R.id.menu_project->{
                    LiveDataBus.send("openDrawer", 0)
                }
                R.id.menu_system->{

                }
                R.id.menu_public->{

                }
                R.id.menu_me->{
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
    }


    override fun initListener() {
        super.initListener()

        LiveDataBus.observe(this, "openDrawer", Observer { result ->
            if (result == 0) {  // 开
                if (!mBinding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mBinding.drawerLayout.openDrawer(Gravity.LEFT)
                }
            } else {  // 关
                if (mBinding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mBinding.drawerLayout.closeDrawer(Gravity.LEFT)
                }
            }
        })
    }

    /**
     * 向导航栏添加Lottie动画
     * added by huyi on 2021/12/06
     */
   /* private fun addLottieAnimationViewToNavigation() {
        val jsonAnimations = listOf(
            "app_main_nav_community.json",
            "app_main_nav_lovecar.json",
            "",
            "app_main_nav_notification.json",
            "app_main_nav_logined.json"
        )
        val navigationView = this.mBinding.navView.getChildAt(0)
        if (navigationView is BottomNavigationMenuView) {
            navigationView.forEachIndexed { index, childView ->
                if (index != 2) { // 过滤掉发布
                    val isLast = index == navigationView.childCount - 1 // 是否是我的
                    if (childView is BottomNavigationItemView) {
                        val iconView: ImageView? = findImageView(childView)
                        if (iconView != null) {
                            val animationJsonName = jsonAnimations[index]
                            val lottieAnimationView = LottieAnimationView(this).apply {
                                this.isGone = true
                                val iconLayoutParams = iconView.layoutParams as FrameLayout.LayoutParams
                                val size = if (isLast) DensityUtil.dp2px(4f) else 0
                                val offset = DensityUtil.dp2px(2f)
                                this.layoutParams = FrameLayout.LayoutParams(
                                    iconLayoutParams.width + size,
                                    iconLayoutParams.height + size
                                ).apply {
                                    this.gravity = iconLayoutParams.gravity
                                    this.leftMargin = iconLayoutParams.leftMargin
                                    // 以下代码是为了优化动画执行完毕后衔接效果
                                    this.topMargin = when (index) {
                                        1 -> iconLayoutParams.topMargin + offset // 爱车
                                        navigationView.childCount - 1 -> iconLayoutParams.topMargin - offset // 我的
                                        else -> iconLayoutParams.topMargin
                                    }
                                    this.rightMargin = iconLayoutParams.rightMargin
                                    this.bottomMargin = iconLayoutParams.bottomMargin
                                }
                                if (isLast) setPadding(2)
                                if (animationJsonName.isNotEmpty()) setAnimation(animationJsonName)
                            }
                            childView.addView(lottieAnimationView) // 添加到容器
                        }
                    }
                }
            }
        }
    }*/

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
