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

        val totalPayment = cartItems.sumOf {
            (it.price.toIntOrNull() ?: 0) * (it.quantity.toIntOrNull() ?: 0)
        }

        binding.tvTotalpembayaran.text = getString(R.string.total_payment_format, totalPayment)

        val itemDetails = cartItems.joinToString("\n") {
            "${it.name} - ${getString(R.string.price_format, it.price)}\nQuantity: ${getString(R.string.quantity_format, it.quantity)}"
        }
        binding.tvPesanan.text = itemDetails

//        if (cartItems.isNotEmpty()) {
//            binding.tvQty.text = getString(R.string.quantity_label, cartItems[0].quantity)
//            binding.tvPrice.text = getString(R.string.price_format, cartItems[0].price)
//        }
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}