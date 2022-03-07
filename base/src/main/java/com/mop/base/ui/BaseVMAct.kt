package com.mop.base.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.mop.base.R
import com.mop.base.app.BaseApp
import com.mop.base.mvvm.BaseModel
import com.mop.base.mvvm.BaseViewModel
import com.mop.base.utils.bus.LiveDataBus
import com.mop.base.utils.loadsir.callback.ErrorCallback
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

// MVVM 基类
abstract class BaseVMAct<V : ViewDataBinding, VM : BaseViewModel<out BaseModel>>(
    @LayoutRes private val layoutId: Int, private val varViewModelId: Int? = null
) : BaseAct<V>(layoutId), ILoading {

    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
        initLoadSir()
    }

    private fun initVM() {
        if (varViewModelId != null) {
            mViewModel = generateVM(this)
            lifecycle.addObserver(mViewModel)
            mBinding.setVariable(varViewModelId, mViewModel)
            //  双向绑定
            mBinding.lifecycleOwner = this
        }
    }


    /**
     * 每个视图肯定会持有一个 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    fun generateVM(viewModelStoreOwner: ViewModelStoreOwner): VM {
        var modelClass: Class<VM>?
        val type: Type? = javaClass.genericSuperclass
        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[1] as? Class<VM>
        } else null
        if (modelClass == null) {  //如果没有指定泛型参数，则默认使用BaseViewModel
            modelClass = BaseViewModel::class.java as Class<VM>
        }
        val app = BaseApp.getInstance()
        val cls = modelClass
        val ow = viewModelStoreOwner
        Log.e("TAG", "initViewModel: ")
        val vm = ViewModelProvider(
            viewModelStoreOwner,
            ViewModelProvider.AndroidViewModelFactory(BaseApp.getInstance())
        ).get(modelClass)
        return vm
    }


    override fun onDestroy() {
        super.onDestroy()

        if (this::mViewModel.isInitialized) {
            lifecycle.removeObserver(mViewModel)
        }

        if (varViewModelId != null) {
            LiveDataBus.removeObserve(this)
            LiveDataBus.removeStickyObserver(this)
            mBinding.unbind()
        }
    }

    /**
     * 如果加载中对话框可手动取消，并且开启了取消耗时任务的功能，则在取消对话框后调用取消耗时任务
     */
    @CallSuper
    fun onCancelLoadingDialog() = mViewModel.cancelConsumingTask()

    private lateinit var mLoadService: LoadService<*>

    @CallSuper
    override fun initLoadSir() { // 只有目标不为空的情况才有实例化的必要
        if (getLoadSirTarget() != null) {
            mLoadService = LoadSir.getDefault().register(getLoadSirTarget()) { onLoadSirReload() } //点击btn才能重试
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
}