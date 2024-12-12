package com.mnrf.ejajan.data.response

data class ApiTransactionRequest(
    val order_id: String,
    val gross_amount: Double,
    val customer_details: CustomerDetails
)

data class CustomerDetails(
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String
)

data class TransactionResponse(
    val status: String,
    val token: String,
    val redirect_url: String
)