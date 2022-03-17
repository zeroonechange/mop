package com.mop.base.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType
import java.util.concurrent.locks.Lock

class CommonExt {

}

/**
 * 判断是否为空 并传入相关操作
 */
inline fun <reified T> T?.notNull(notNullAction: (T) -> Unit, nullAction: () -> Unit = {}) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction.invoke()
    }
}

/**
 * 高阶函数是将函数用作参数或返回值的函数
 * todo
 * 内联函数  提高性能
 */

inline fun <T> lock(lock: Lock, body: () -> T): T {

    return body.invoke()
}


@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> AppCompatActivity.inflateBindingWithGeneric(layoutInflater: LayoutInflater): VB =
    withGenericBindingClass<VB>(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
    }.also { binding ->
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = this
        }
    }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Fragment.inflateBindingWithGeneric(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB =
    withGenericBindingClass<VB>(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
            .invoke(null, layoutInflater, parent, attachToParent) as VB
    }.also { binding ->
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }


private fun <VB : ViewBinding> withGenericBindingClass(any: Any, block: (Class<VB>) -> VB): VB {
    var genericSuperclass = any.javaClass.genericSuperclass
    var superclass = any.javaClass.superclass
    while (superclass != null) {
        if (genericSuperclass is ParameterizedType) {
            try {
                return block.invoke(genericSuperclass.actualTypeArguments[0] as Class<VB>)
            } catch (e: NoSuchMethodException) {
            } catch (e: ClassCastException) {
            } catch (e: InvocationTargetException) {
                throw e.targetException
            }
        }
        genericSuperclass = superclass.genericSuperclass
        superclass = superclass.superclass
    }
    throw IllegalArgumentException("There is no generic of ViewBinding.")
}