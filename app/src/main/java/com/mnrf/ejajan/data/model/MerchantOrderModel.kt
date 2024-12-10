package com.mnrf.ejajan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MerchantOrderModel(
    val id: String,
    val merchantUid: String,
    val studentUid: String,
    val menuId: String,
    val menuName: String,
    val menuImage: String,
    val menuQty: String,
    val menuPrice: String,
    val orderPickupTime: String,
    val orderStatus: String
) : Parcelable