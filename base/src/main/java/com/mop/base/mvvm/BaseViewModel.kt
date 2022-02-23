package com.mop.base.mvvm

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kingja.loadsir.callback.Callback
import com.mop.base.app.RepositoryManager
import com.mop.base.data.HttpHandler
import com.mop.base.utils.bus.LiveDataBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Call
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

open class BaseViewModel<M : BaseModel>(app: Application) : AndroidViewModel(app), IViewModel {

    /**
     * 是否自动创建仓库，默认是 true，
     */
    private var isAutoCreateRepo = true

    /**
     * 是否缓存自动创建的仓库，默认是 true
     */
    protected open fun isCacheRepo() = true

    /**
     * 可能存在没有仓库的 vm，但我们这里也不要是可 null 的。
     * 如果 vm 没有提供仓库，说明此变量不可用，还去使用的话自然就报错。
     */
    lateinit var mModel: M

    private lateinit var mCallList: MutableList<Call<*>>

    internal val mUiChangeLiveData by lazy { UiChangeLiveData() }

    open var onNoLogin: (() -> Unit)? = null
    open var onNoData: (() -> Unit)? = null

    constructor(app: Application, model: M) : this(app) {
        isAutoCreateRepo = false
        mModel = model
    }

    init {
        createRepo()
    }

    /**
     * 创建Repository
     */
    fun createRepo() {
        if (isAutoCreateRepo) {
            if (!this::mModel.isInitialized) {
                val modelClass: Class<M>?
                val type: Type? = javaClass.genericSuperclass
                modelClass = if (type is ParameterizedType) {
                    @Suppress("UNCHECKED_CAST") type.actualTypeArguments[0] as? Class<M>
                } else null
                if (modelClass != null && modelClass != BaseModel::class.java) {
                    mModel = RepositoryManager.getRepo(modelClass, isCacheRepo())
                }
            }
        }
    }

    @CallSuper
    override fun onCleared() { // 可能 mModel 是未初始化的
        if (this::mModel.isInitialized) {
            mModel.onCleared()
        }

        LiveDataBus.removeObserve(this)
        LiveDataBus.removeStickyObserver(this)
        cancelConsumingTask()
    }

    /**
     * 取消耗时任务，比如在界面销毁时，或者在对话框消失时
     */
    fun cancelConsumingTask() { // ViewModel销毁时会执行，同时取消所有异步任务
        if (this::mCallList.isInitialized) {
            mCallList.forEach { it.cancel() }
            mCallList.clear()
        }
        viewModelScope.cancel()
    }


    /**
     * 所有网络请求都在 mCoroutineScope 域中启动协程，当页面销毁时会自动取消
     */
    fun <T> launch(
        block: suspend CoroutineScope.() -> IBaseResponse<T?>?,
        onSuccess: (() -> Unit)? = null,
        onResult: ((t: T) -> Unit),
        onFailed: ((code: Int, msg: String?) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                HttpHandler.handleResult(block(), onSuccess, onResult, onFailed, onNoLogin, onNoData)
            } catch (e: Exception) {
                onFailed?.let { HttpHandler.handleException(e, it) }
            } finally {
                onComplete?.invoke()
            }
        }
    }

    /**
     * 发起协程，让协程和 UI 相关
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { block() }
    }

    /**
     * 发起流
     */
    fun <T> launchFlow(block: suspend () -> T): Flow<T> {
        return flow {
            emit(block())
        }
    }


    /**
     * 通用的 Ui 改变变量
     */
    class UiChangeLiveData {
        var showLoadingDialogEvent: SingleLiveEvent<String?>? = null
        var dismissLoadingDialogEvent: SingleLiveEvent<Any?>? = null
        var startActivityEvent: String? = null
        var finishEvent: String? = null
        var loadSirEvent: SingleLiveEvent<Class<out Callback>?>? = null
    }

}