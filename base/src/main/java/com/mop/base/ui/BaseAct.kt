package com.mop.base.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mop.base.R
import com.mop.base.widget.CustomLayoutDialog

// 基类 简单功能使用  不带MVVM
abstract class BaseAct<V : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    AppCompatActivity() {

    protected val TAG: String by lazy { javaClass.simpleName }

    protected lateinit var mBinding: V

    override fun onCreate(savedInstanceState: Bundle?) {
//        Utils.setStatusBar(this)
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.inflate(layoutInflater, layoutId, null, false)
        setContentView(mBinding.root)
        initView()
        initData()
    }


    open fun initView() {

    }

    open fun initData() {

    }


    override fun setContentView(id: Int) {
        setContentView(layoutInflater.inflate(id, null))
    }

    override fun setContentView(v: View) {
        setContentView(
            v,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    // 保证只有主线程访问这个变量，所以 lazy 不需要同步机制
    private val mLoadingDialog by lazy(mode = LazyThreadSafetyMode.NONE) {
        CustomLayoutDialog(this, R.layout.mvvm_dlg_loading)
    }

}