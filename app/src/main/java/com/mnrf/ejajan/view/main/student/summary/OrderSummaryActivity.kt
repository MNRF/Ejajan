package com.mnrf.ejajan.view.main.student.summary

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.pref.CartPreferences
import com.mnrf.ejajan.databinding.ActivityStudentCartBinding
import com.mnrf.ejajan.databinding.ActivityStudentOrderSummaryBinding
import com.mnrf.ejajan.view.login.LoginStudent

class OrderSummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentOrderSummaryBinding
    private lateinit var cartPreferences: CartPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentOrderSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartPreferences = CartPreferences(this)

        binding.btnFinish.setOnClickListener {
            cartPreferences.clearCartItems()
            startActivity(Intent(this, LoginStudent::class.java))
            finish()
        }
    }
}