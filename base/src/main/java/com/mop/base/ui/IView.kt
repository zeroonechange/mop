package com.mop.base.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.mop.base.app.BaseApp
import com.mop.base.mvvm.BaseModel
import com.mop.base.mvvm.BaseViewModel
import com.mop.base.utils.bus.LiveDataBus
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * V 层，这里的视图都是 Activity 或 Fragment
 */
interface IView<V : ViewBinding, VM : BaseViewModel<out BaseModel>> {
    /**
     * 初始化外部传进来的参数
     */
    fun initParam() {}

    /**
     * 初始化标题等
     */
    fun initToolbar() {}

    /**
     * 初始化界面观察者
     */
    fun initViewObservable() {}

    /**
     * 初始化数据
     */
    fun initData() {}

    /**
     * 监听点击、滑动事件
     */
    fun initListener() {}

    /**
     * 初始化 DataBinding，基类应该在初始化后设为 final
     */
    fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V

    /**
     * 初始化视图和 VM
     */
    fun initViewModel()

    /**
     * 初始化通用的 UI 改变事件，基类应该在初始化后设为 final
     */
    fun initUiChangeLiveData()

    /**
     * 移除事件总线监听，避免内存泄露
     */
    fun removeLiveDataBus(owner: LifecycleOwner) {
        LiveDataBus.removeObserve(owner)
        LiveDataBus.removeStickyObserver(this)
    }

    /**
     * 每个视图肯定会持有一个 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    fun initViewModel(viewModelStoreOwner: ViewModelStoreOwner): VM {
        var modelClass: Class<VM>?
        val type: Type? = javaClass.genericSuperclass
        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[1] as? Class<VM>
        } else null
        if (modelClass == null) {
            modelClass = BaseViewModel::class.java as Class<VM>
        } //如果没有指定泛型参数，则默认使用BaseViewModel
        BaseViewModel::class.java
        val vm = ViewModelProvider(
            viewModelStoreOwner, ViewModelProvider.AndroidViewModelFactory(BaseApp.getInstance())
        ).get(modelClass) // 让 vm 也可以直接获取到 bundle
        return vm
    }
}