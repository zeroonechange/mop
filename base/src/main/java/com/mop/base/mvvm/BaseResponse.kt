package com.mop.base.mvvm


data class BaseResponse<T>(
    var data: T?,
    var code: Int?,
    var message: String?,
    var successful: Boolean?
) : IBaseResponse<T> {
    override fun code() = code

    override fun msg() = message

    override fun data() = data

    override fun isSuccess() = code == 0
}