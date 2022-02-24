package com.mop.base.data

import com.mop.base.mvvm.BaseModel
import com.mop.base.mvvm.BaseResponse
import kotlinx.coroutines.flow.Flow

class MyRepository : BaseModel() {

    private val findService = HttpRequest.getService(
        ApiService::class.java
    )

    fun queryBanner(): Flow<BaseResponse<ArrayList<Any>>> {
        return flowEmit {
            findService.getBanner()
        }
    }
}