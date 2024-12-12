package com.mnrf.ejajan.view.main.student.summary

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.model.CartModel
import com.mnrf.ejajan.data.pref.CartPreferences
import com.mnrf.ejajan.databinding.ActivityStudentOrderSummaryBinding
import com.mnrf.ejajan.view.login.LoginStudent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderSummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentOrderSummaryBinding
    private lateinit var cartPreferences: CartPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentOrderSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartPreferences = CartPreferences(this)

        val cartItems = cartPreferences.getCartItems()
        displayOrderSummary(cartItems)

        binding.btnFinish.setOnClickListener {
            cartPreferences.clearCartItems()
            startActivity(Intent(this, LoginStudent::class.java))
            finish()
        }
    }

    private fun displayOrderSummary(cartItems: List<CartModel>) {
        val currentTime = getCurrentTime()
        binding.tvTgl.text = getString(R.string.waktu_pemesanan_label, currentTime)

        // Hitung total pembayaran
        val totalPayment = cartItems.sumOf { cartItem ->
            val discountedPrice = cartItem.discountedPrice?.toIntOrNull() // Harga diskon
            val normalPrice = cartItem.price.toIntOrNull() ?: 0 // Harga normal
            val priceToUse = if (discountedPrice != null && discountedPrice > 0) discountedPrice else normalPrice
            priceToUse * (cartItem.quantity.toIntOrNull() ?: 0)
        }
        binding.tvTotalpembayaran.text = getString(R.string.total_payment_format, totalPayment)

        // Buat string detail pesanan
        val itemDetails = cartItems.joinToString("\n") { cartItem ->
            val discountedPrice = cartItem.discountedPrice?.toIntOrNull()
            val normalPrice = cartItem.price.toIntOrNull() ?: 0

            if (discountedPrice != null && discountedPrice > 0) {
                // Tampilkan harga diskon dan harga normal dicoret
                "${cartItem.name}\n" +
                        "${getString(R.string.price_format_normal, normalPrice)} -> ${getString(R.string.price_format_special, discountedPrice)}\n" +
                        "Quantity: ${cartItem.quantity}"
            } else {
                // Tampilkan hanya harga normal
                "${cartItem.name} - ${getString(R.string.price_format_special, normalPrice)}\n" +
                        "Quantity: ${cartItem.quantity}"
            }
        }
        binding.tvPesanan.text = itemDetails

    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
