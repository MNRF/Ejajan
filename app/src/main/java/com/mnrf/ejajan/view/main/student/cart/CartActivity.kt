package com.mnrf.ejajan.view.main.student.cart

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.pref.CartPreferences
import com.mnrf.ejajan.databinding.ActivityStudentCartBinding
import com.mnrf.ejajan.view.main.student.adapter.CartAdapter
import com.mnrf.ejajan.view.main.student.adapter.OrderSummaryAdapter
import com.mnrf.ejajan.view.main.student.face.FaceConfirmActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentCartBinding

    private lateinit var adapter: CartAdapter
    private lateinit var cartPreferences: CartPreferences
    private lateinit var summaryAdapter: OrderSummaryAdapter

    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactory.getInstance(this@CartActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Cart Item"
        }

        cartPreferences = CartPreferences(this)
        val cartItems = cartPreferences.getCartItems()
        cartPreferences.getItemSummaries()

        adapter = CartAdapter(cartItems)
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.adapter = adapter

        summaryAdapter = OrderSummaryAdapter()
        binding.rvOrderSummary.layoutManager = LinearLayoutManager(this)
        binding.rvOrderSummary.adapter = summaryAdapter

        val totalPayment = cartItems.sumOf {
            (it.price.toIntOrNull() ?: 0) * (it.quantity.toIntOrNull() ?: 0)
        }

        binding.tvTotalPayment.text = getString(R.string.price, totalPayment)

        binding.btnConfirm.setOnClickListener {
            val totalTransaction = binding.tvTotalPayment.text.toString()
                .replace("[^0-9]".toRegex(), "")

            cartViewModel.fetchUidsAndCreateTransaction(totalTransaction) {
                startActivity(Intent(this, FaceConfirmActivity::class.java))
            }
        }

        binding.btnCancel.setOnClickListener {
            cartPreferences.clearCartItems()
            cartPreferences.clearSummaryItems()
            adapter.notifyDataSetChanged()
            binding.tvTotalPayment.text = getString(R.string.price_format, 0.toString())
            startActivity(Intent(this, CartActivity::class.java))
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}