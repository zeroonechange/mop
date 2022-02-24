package com.mop.base.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.mop.base.R
import com.mop.base.data.config.GlobalConfig
import com.mop.base.utils.DensityUtil.dp2px
import com.mop.base.utils.ScreenUtils
import com.mop.base.utils.Utils
import com.mop.base.widget.PagerEnabledSlidingPaneLayout


abstract class ParallaxSwipeBackActivity : AppCompatActivity() {
    // 父布局
    private var mSlidingPaneLayout: PagerEnabledSlidingPaneLayout? = null
    private var mFrameLayout: FrameLayout? = null

    // 背景图
    private var mBehindImageView: ImageView? = null

    // 阴影图
    private var mShadowImageView: ImageView? = null
    private var mDefaultTranslationX = 100
    private var mShadowWidth = 20
    public var isFull: Boolean = true
    open fun full() {
        isFull = true
    }

    //返回true代表已经设置了软键盘mode
    open fun setSoftInputMode(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (isSupportSwipe()) {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


        full()
        if (isFull) {
            Utils.setStatusBar(this)
        }

        if (!setSoftInputMode()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }

        super.onCreate(savedInstanceState)
    }

    private fun initSliding() { //通过反射来改变SlidingPanelayout的值
        try {
            mSlidingPaneLayout = PagerEnabledSlidingPaneLayout(this)
            val overhangSize = SlidingPaneLayout::class.java.getDeclaredField("mOverhangSize")
            overhangSize.isAccessible = true
            overhangSize[mSlidingPaneLayout] = 0
            mSlidingPaneLayout!!.setPanelSlideListener(SlidingListener())
            mSlidingPaneLayout!!.sliderFadeColor = Color.TRANSPARENT
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mDefaultTranslationX = dp2px(mDefaultTranslationX.toFloat())
        mShadowWidth = dp2px(mShadowWidth.toFloat())

        // 背景
        val behindFrameLayout = FrameLayout(this)
        mBehindImageView = ImageView(this)
        mBehindImageView!!.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
        )
        behindFrameLayout.addView(mBehindImageView, 0)

        // 容器
        val containerLayout = LinearLayout(this)
        containerLayout.orientation = LinearLayout.HORIZONTAL
        containerLayout.setBackgroundColor(Color.TRANSPARENT)
        containerLayout.layoutParams = ViewGroup.LayoutParams(
            ScreenUtils.getScreenWidth() + mShadowWidth, ViewGroup.LayoutParams.MATCH_PARENT
        ) //you view container
        mFrameLayout = FrameLayout(this)
        mFrameLayout!!.setBackgroundColor(Color.WHITE)
        mFrameLayout!!.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )

        //add shadow
        mShadowImageView = ImageView(this)
        mShadowImageView!!.setBackgroundResource(R.drawable.mvvm_swipe_back_shadow)
        mShadowImageView!!.layoutParams = LinearLayout.LayoutParams(
            mShadowWidth, LinearLayout.LayoutParams.MATCH_PARENT
        ) // 阴影在左，容器在右
        containerLayout.addView(mShadowImageView)
        containerLayout.addView(mFrameLayout)
        containerLayout.translationX = -mShadowWidth.toFloat() // 添加背景和容器
        mSlidingPaneLayout!!.addView(behindFrameLayout, 0)
        mSlidingPaneLayout!!.addView(containerLayout, 1)
    }

    override fun setContentView(id: Int) {
        setContentView(layoutInflater.inflate(id, null))

    }

    override fun setContentView(v: View) {
        setContentView(
            v, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        if (mBehindImageView != null) {
            mBehindImageView!!.scaleType = ImageView.ScaleType.FIT_XY
            mBehindImageView!!.setImageResource(R.drawable.mvvm_swipe_back_shadow_bg)
        }

    }

    /**
     * 由于要实现侧滑， windowIsTranslucent 必须为 true，但是设置为 true 后，activity 就相当于半透明的效果了。
     * 因此，在此activity之下的activity，并不会走 onStop
     */
    override fun onStop() {
        super.onStop()
    }

    override fun setContentView(
        v: View, params: ViewGroup.LayoutParams
    ) {
        if (isSupportSwipe()) {
            initSliding() // 设置根view
            super.setContentView(mSlidingPaneLayout, params) // 再添加自己的view
            mFrameLayout!!.removeAllViews()
            mFrameLayout!!.addView(v, params)
        } else {
            super.setContentView(v, params)
        }

    }

    /**
     * 是否支持侧滑返回
     */
    protected open fun isSupportSwipe(): Boolean = GlobalConfig.gIsSupportSwipe

    private inner class SlidingListener : SlidingPaneLayout.PanelSlideListener {
        override fun onPanelClosed(view: View) {}
        override fun onPanelOpened(view: View) {
            finish()
            overridePendingTransition(0, 0)
        }

        /**
         * @param v 滑动时从左右到右是 0-1
         */
        override fun onPanelSlide(view: View, v: Float) { //duang duang duang 你可以在这里加入很多特效
            mBehindImageView!!.translationX = v * mDefaultTranslationX - mDefaultTranslationX
            mBehindImageView!!.alpha = 1 - v
            mShadowImageView!!.alpha = 1 - v
        }
    }


}