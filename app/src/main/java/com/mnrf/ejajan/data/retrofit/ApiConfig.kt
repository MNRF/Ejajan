package com.mnrf.ejajan.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val CREATE_TRANSACTION_BASE_URL = "https://capstone-c242-ps370.et.r.appspot.com/"
    private const val CHECK_STATUS_BASE_URL = "https://app.sandbox.midtrans.com/"

    fun getCreateTransactionService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(CREATE_TRANSACTION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun getCheckTransactionStatusService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(CHECK_STATUS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

