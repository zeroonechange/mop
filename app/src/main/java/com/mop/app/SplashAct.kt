package com.mop.app

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mop.app.databinding.ActivitySplashBinding
import com.mop.app.viewmodel.SplashVM
import com.mop.base.base.act.BaseVBAct
import kotlinx.android.synthetic.main.activity_splash.*
import kotlin.system.exitProcess

class SplashAct : BaseVBAct<SplashVM, ActivitySplashBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
//        Utils.setStatusBar(this)
//        StatusBarUtils.setLightStatusBar(this, true)
        iv.setOnClickListener {

        }

        tv.setOnClickListener {
            launchHomeScreen()
        }
    }

    override fun initData() {
        val imageUrl = "https://zeroonechange.github.io/img/3.jpg"
        Glide.with(this).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);

        cdt = MyCountDownTimer(2000, 1000, tv)
        cdt?.start()
    }


    private fun launchHomeScreen() {
        var it = Intent(
            this@SplashAct,
            MainActivity::class.java
        ) // it.putExtra(RouterConstant.IS_FIRST_TIME_LANCH, false)
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