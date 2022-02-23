package com.mop.base.data

import com.mop.base.mvvm.BaseResponse
import retrofit2.http.*


interface ApiService {

    companion object {
        const val SERVER_URL = "https://wanandroid.com/"
    }

    @GET("banner/json")
    suspend fun getBanner(): BaseResponse<ArrayList<Any>>

    /**
     * 完成一个TODO
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    suspend fun doneTodo(@Path("id") id: Int, @Field("status") status: Int): BaseResponse<Any?>
}