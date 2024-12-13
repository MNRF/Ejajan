package com.mnrf.ejajan.data.retrofit

import com.mnrf.ejajan.data.response.TransactionResponse
import com.mnrf.ejajan.data.response.ApiTransactionRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/")
    fun createTransaction(@Body request: ApiTransactionRequest): Call<TransactionResponse>

    @GET("snap/v1/transactions/{order_id}/status")
    fun getTransactionStatus(@Path("order_id") orderId: String): Call<TransactionResponse>
}
