package com.mop.base.data

import com.mop.base.mvvm.BaseModel

class MyRepository : BaseModel(){

    private val findService = HttpRequest.getService(
        ApiService::class.java
    )

}