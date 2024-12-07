package com.mnrf.ejajan.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mnrf.ejajan.data.model.CartModel
import com.mnrf.ejajan.data.model.NotesModel
import com.mnrf.ejajan.data.model.OrderSummaryModel

class CartPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)

    fun saveCartItems(cartItems: List<CartModel>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(cartItems)
        editor.putString("cart_items", json)
        editor.apply()
    }

    fun getCartItems(): List<CartModel> {
        val json = sharedPreferences.getString("cart_items", "[]")
        val type = object : TypeToken<List<CartModel>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun clearCartItems() {
        val editor = sharedPreferences.edit()
        editor.remove("cart_items")
        editor.apply()
    }

    fun saveItemSummaries(itemSummaries: List<OrderSummaryModel>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(itemSummaries)
        editor.putString("item_summaries", json)
        editor.apply()
    }

    fun getItemSummaries(): List<OrderSummaryModel> {
        val json = sharedPreferences.getString("item_summaries", "[]")
        val type = object : TypeToken<List<OrderSummaryModel>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun clearSummaryItems() {
        val editor = sharedPreferences.edit()
        editor.remove("item_summaries")
        editor.apply()
    }
}