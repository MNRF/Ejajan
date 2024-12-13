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
    val token: String?, // Snap token
    val status_code: String?, // HTTP-like status code
    val transaction_id: String?,
    val gross_amount: String?,
    val currency: String?,
    val order_id: String?,
    val payment_type: String?,
    val transaction_status: String?, // Key status like "settlement", "pending"
    val fraud_status: String?,
    val status_message: String?,
    val merchant_id: String?,
    val va_numbers: List<VANumber>?, // For virtual accounts
    val payment_amounts: List<String>?,
    val transaction_time: String?,
    val settlement_time: String?,
    val expiry_time: String?,
    val finish_redirect_url: String?
)

data class VANumber(
    val bank: String?,
    val va_number: String?
)

