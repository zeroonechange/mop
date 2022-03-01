package com.mop.app

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mop.app.data.SplashVM
import com.mop.app.databinding.ActivitySplashBinding
import com.mop.base.ui.DataBindingBaseActivity
import com.mop.base.utils.Utils
import com.mop.base.utils.statusbar.StatusBarUtils
import kotlin.system.exitProcess

class SplashAct : DataBindingBaseActivity<ActivitySplashBinding, SplashVM>(R.layout.activity_splash, BR.viewModel) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setStatusBar(this)
        StatusBarUtils.setLightStatusBar(this, true)
    }

    override fun initViewModel() {
        super.initViewModel()
    }

    override fun initListener() {
        super.initListener()

        mBinding.iv.setOnClickListener {

        }

        mBinding.tv.setOnClickListener {
            launchHomeScreen()
        }

        val imageUrl = "https://zeroonechange.github.io/img/bbb.jpg"
        Glide.with(this).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(mBinding.iv);

        cdt = MyCountDownTimer(5000, 1000, mBinding.tv)
        cdt?.start()

    }

    private fun launchHomeScreen() {
        var it = Intent(this@SplashAct, MainActivity::class.java) // it.putExtra(RouterConstant.IS_FIRST_TIME_LANCH, false)
        startActivity(it)
        finish()
    }

    private var cdt: MyCountDownTimer? = null

    /**
     * 倒计时
     */
    private inner class MyCountDownTimer(
        millisInFuture: Long, countDownInterval: Long, internal var tv: TextView
    ) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            launchHomeScreen()
        }

        override fun onTick(millisUntilFinished: Long) {
            if (millisUntilFinished / 1000 > 0) {
                tv.text = "${millisUntilFinished / 1000}S | 跳过"
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
        exitProcess(0)
    }

}