package com.mop.base.mvvm

import com.mop.base.utils.FlowUtil
import kotlinx.coroutines.flow.Flow

/**
 * Model 层的基类
 */
abstract class BaseModel : IModel {

    override fun onCleared() {}

    /**
     * 发起流
     */
    fun <T> flowEmit(block: suspend () -> T): Flow<T> {
        return FlowUtil.flowEmit(block)
    }
}