package com.mnrf.ejajan.data.retrofit

import com.mnrf.ejajan.data.response.TransactionResponse
import com.mnrf.ejajan.data.response.ApiTransactionRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/")
    fun createTransaction(@Body request: ApiTransactionRequest): Call<TransactionResponse>
}

