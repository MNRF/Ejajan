package com.mnrf.ejajan.data.repository

import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.mnrf.ejajan.data.model.CartItem

class CartRepository(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)

    fun getCartItems(): List<CartItem> {
        val jsonString = sharedPreferences.getString("cart_items", "[]") ?: "[]"
        val gson = Gson()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    fun saveCartItems(cartItems: List<CartItem>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonString = gson.toJson(cartItems)
        editor.putString("cart_items", jsonString)
        editor.apply()
    }
}
