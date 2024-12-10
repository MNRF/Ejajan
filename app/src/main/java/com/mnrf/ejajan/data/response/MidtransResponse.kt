package com.mnrf.ejajan.data.response

import com.google.gson.annotations.SerializedName

data class MidtransResponse (

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)