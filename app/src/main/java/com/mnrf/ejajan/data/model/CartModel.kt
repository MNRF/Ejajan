package com.mnrf.ejajan.data.model

data class CartModel(
    val id: String,
    val name: String,
    val price: String,
    val quantity: String,
    val imageurl: String,
    val discountedPrice: String? = null
)