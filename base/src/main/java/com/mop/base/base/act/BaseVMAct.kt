package com.mop.base.base.act

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.mop.base.base.BaseApp
import com.mop.base.base.viewmodel.BaseViewModel
import com.mop.base.ext.notNull
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseVMAct<VM : BaseViewModel> : AppCompatActivity() {

    companion object{
        val TAG = javaClass.simpleName.toString()
    }
    abstract fun layoutID(): Int
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBind().notNull({
            setContentView(it)
        }, {
            setContentView(layoutID())
        })
        viewModel = createViewModel(this)
        initView(savedInstanceState)
        initData()
    }

    private fun createViewModel(viewModelStoreOwner: ViewModelStoreOwner): VM {
        var modelClass: Class<VM>?
        val type: Type? = javaClass.genericSuperclass
        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[0] as? Class<VM>
        } else null
        if (modelClass == null) {
            modelClass = BaseViewModel::class.java as Class<VM>
        }
        return ViewModelProvider(
            viewModelStoreOwner,
            ViewModelProvider.AndroidViewModelFactory(BaseApp.instance!!)
        ).get(modelClass)
    }

    abstract fun initData()

    abstract fun initView(savedInstanceState: Bundle?)

    open fun initDataBind(): View? {
        return null
    }
}