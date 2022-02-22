package com.mop.base.utils

import com.mop.base.data.HttpHandler
import com.mop.base.mvvm.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * @author : wfg
 * @date   : 2022-01-22
 * @desc   : flow工具类
 */
object FlowUtil {

    /**
     * 网络请求发起流
     */
    fun <T> flowEmit(block: suspend () -> T): Flow<T> {
        return flow {
            var result = block()
            if(result is ResultResponse<*>){
                result.onResult()
            }
            emit(result)
        }.catch { e->
            var result = HttpHandler.handleException(e)
            emit(result as T)
        }.flowOn(Dispatchers.IO)
    }
}