package com.mop.base.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.mop.base.utils.loadsir.callback.ErrorCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.mop.base.R
import com.mop.base.mvvm.BaseModel
import com.mop.base.mvvm.BaseViewModel
import com.mop.base.widget.CustomLayoutDialog

/**
 * 通过构造函数和泛型，完成 view 的初始化和 vm 的初始化，并且将它们绑定，
 */
abstract class ViewBindingBaseActivity<V : ViewBinding, VM : BaseViewModel<out BaseModel>> : ParallaxSwipeBackActivity(), IView<V, VM>, ILoadingDialog, ILoading {

    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM

    // 保证只有主线程访问这个变量，所以 lazy 不需要同步机制
    private val mLoadingDialog by lazy(mode = LazyThreadSafetyMode.NONE) { CustomLayoutDialog(this, loadingDialogLayout()) }

    private lateinit var mLoadService: LoadService<*>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = initBinding(layoutInflater, null)
        initContentView(mBinding.root)
        initViewModel()
        initParam()
        initToolbar()
        initUiChangeLiveData()
        initViewObservable()
        initLoadSir()
        initListener()
        initData()
    }

    abstract override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V

    open fun initContentView(contentView: View) {
        setContentView(contentView)
    }

    @CallSuper
    override fun initViewModel() {
        mViewModel = initViewModel(this)
        lifecycle.addObserver(mViewModel)
    }

    @CallSuper
    override fun initUiChangeLiveData() {
        if (isNeedLoadingDialog()) {
            mViewModel.mUiChangeLiveData.initLoadingDialogEvent() // 显示对话框
            mViewModel.mUiChangeLiveData.showLoadingDialogEvent?.observe(this, Observer {
                showLoadingDialog(mLoadingDialog, it)
            }) // 隐藏对话框
            mViewModel.mUiChangeLiveData.dismissLoadingDialogEvent?.observe(this, Observer {
                dismissLoadingDialog(mLoadingDialog)
            })
        }
    }

    @CallSuper
    override fun initLoadSir() { // 只有目标不为空的情况才有实例化的必要
        if (getLoadSirTarget() != null) {
            mLoadService = LoadSir.getDefault().register(
                getLoadSirTarget()
            ) { onLoadSirReload() } //点击btn才能重试
            mLoadService.setCallBack(ErrorCallback::class.java) { _, view ->
                view.findViewById<Button>(R.id.error_btn).setOnClickListener {
                    onLoadSirReload()
                }
            }

            mViewModel.mUiChangeLiveData.initLoadSirEvent()
            mViewModel.mUiChangeLiveData.loadSirEvent?.observe(this, Observer {
                if (it == null) {
                    mLoadService.showSuccess()
                    onLoadSirSuccess()
                } else {
                    mLoadService.showCallback(it)
                    onLoadSirShowed(it)
                }
            })
        } else {
            Log.d("0219", "getLoadSirTarget==null")
        }
    }

    /**
     * <pre>
     *     // 一开始我们这么写
     *     mViewModel.liveData.observe(this, Observer { })
     *
     *     // 用这个方法可以这么写
     *     observe(mViewModel.liveData) { }
     *
     *     // 或者这么写
     *     observe(mViewModel.liveData, this::onChanged)
     *     private fun onChanged(s: String) { }
     * </pre>
     */
    fun <T> observe(liveData: LiveData<T>, onChanged: ((t: T) -> Unit)) = liveData.observe(this, Observer { onChanged(it) })

    /**
     * 如果加载中对话框可手动取消，并且开启了取消耗时任务的功能，则在取消对话框后调用取消耗时任务
     */
    @CallSuper
    override fun onCancelLoadingDialog() = mViewModel.cancelConsumingTask()

    override fun onDestroy() {
        super.onDestroy()

        // 界面销毁时移除 vm 的生命周期感知
        if (this::mViewModel.isInitialized) {
            lifecycle.removeObserver(mViewModel)
        }
        removeLiveDataBus(this)
    }
}