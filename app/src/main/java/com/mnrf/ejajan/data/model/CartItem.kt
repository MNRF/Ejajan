package com.mnrf.ejajan.data.model

data class CartItem(
    val menuUid: String,
    val merchantUid: String,
    val menuName: String,
    val menuDescription: String,
    val menuPrice: String,
    val menuPreparationTime: String,
    val menuQty: String,
    val menuImageFile: String
)