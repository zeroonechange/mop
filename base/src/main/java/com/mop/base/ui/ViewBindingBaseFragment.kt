package com.mop.base.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.CallSuper
import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.mop.base.R
import com.mop.base.mvvm.BaseModel
import com.mop.base.mvvm.BaseViewModel
import com.mop.base.utils.Utils
import com.mop.base.utils.loadsir.callback.ErrorCallback
import com.mop.base.widget.CustomLayoutDialog

abstract class ViewBindingBaseFragment<V : ViewBinding, VM : BaseViewModel<out BaseModel>>(
    private val sharedViewModel: Boolean = false
) : Fragment(),
    IView<V, VM>, ILoadingDialog, ILoading {

    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM

    private lateinit var mStartActivityForResult: ActivityResultLauncher<Intent>

    private val mLoadingDialog by lazy(mode = LazyThreadSafetyMode.NONE) {
        CustomLayoutDialog(
            requireActivity(),
            loadingDialogLayout()
        )
    }

    private lateinit var mLoadService: LoadService<*>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //mBinding还未初始化
        mBinding = initBinding(inflater, container)
        if (getLoadSirTarget() == null) {
            return mBinding.root
        } else {
            if (getLoadSirTarget() is View) {
                mLoadService =
                    LoadSir.getDefault().register(getLoadSirTarget()) { onLoadSirReload() }

                //点击btn才能重试
                mLoadService.setCallBack(ErrorCallback::class.java) { _, view ->
                    view.findViewById<Button>(R.id.error_btn).setOnClickListener {
                        onLoadSirReload()
                    }
                }

                return mBinding.root
            } else {
                mLoadService =
                    LoadSir.getDefault().register(
                        mBinding.root
                    ) { onLoadSirReload() }

                //点击btn才能重试
                mLoadService.setCallBack(ErrorCallback::class.java) { _, view ->
                    view.findViewById<Button>(R.id.error_btn).setOnClickListener {
                        onLoadSirReload()
                    }
                }
                return mLoadService.loadLayout
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initParam()
        initToolbar()
        initUiChangeLiveData()
        initViewObservable()
        initLoadSir()
        initData()
        initListener()
    }

    @CallSuper
    override fun initViewModel() {
        mViewModel = if (sharedViewModel) {
            initViewModel(requireActivity())
        } else {
            initViewModel(this)
        }
        // 让 vm 可以感知 v 的生命周期
        lifecycle.addObserver(mViewModel)
    }

    @CallSuper
    override fun initUiChangeLiveData() {
        if (isNeedLoadingDialog()) {
            mViewModel.mUiChangeLiveData.initLoadingDialogEvent()

            // 显示对话框
            mViewModel.mUiChangeLiveData.showLoadingDialogEvent?.observe(this, Observer {
                showLoadingDialog(mLoadingDialog, it)
            })
            // 隐藏对话框
            mViewModel.mUiChangeLiveData.dismissLoadingDialogEvent?.observe(this, Observer {
                dismissLoadingDialog(mLoadingDialog)
            })
        }
    }

    @CallSuper
    override fun initLoadSir() {
        // 只有目标不为空的情况才有实例化的必要
        if (getLoadSirTarget() != null) {
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
        }
    }

    fun startActivity(
        clz: Class<out Activity>?,
        map: ArrayMap<String, *>? = null,
        bundle: Bundle? = null
    ) {
        startActivity(Utils.getIntentByMapOrBundle(activity, clz, map, bundle))
    }

    fun startActivityForResult(
        clz: Class<out Activity>?,
        map: ArrayMap<String, *>? = null,
        bundle: Bundle? = null
    ) {
        mStartActivityForResult.launch(Utils.getIntentByMapOrBundle(activity, clz, map, bundle))
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
    fun <T> observe(liveData: LiveData<T>, onChanged: ((t: T) -> Unit)) =
        liveData.observe(this, Observer { onChanged(it) })

    /**
     * 如果加载中对话框可手动取消，并且开启了取消耗时任务的功能，则在取消对话框后调用取消耗时任务
     */
    @CallSuper
    override fun onCancelLoadingDialog() = mViewModel.cancelConsumingTask()

    override fun onDestroyView() {
        super.onDestroyView()
        Utils.releaseBinding(this.javaClass, ViewBindingBaseFragment::class.java, this, "mBinding")
    }

    override fun onDestroy() {
        super.onDestroy()

        // 界面销毁时移除 vm 的生命周期感知
        if (this::mViewModel.isInitialized) {
            lifecycle.removeObserver(mViewModel)
        }
        removeLiveDataBus(this)
    }
}