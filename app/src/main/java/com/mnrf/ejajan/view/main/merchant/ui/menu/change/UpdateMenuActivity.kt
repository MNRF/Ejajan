package com.mnrf.ejajan.view.main.merchant.ui.menu.change

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R
import com.mnrf.ejajan.view.main.merchant.ui.menu.MenuMerchantFragment

class UpdateMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_update_menu)

        val btnUbah = findViewById<Button>(R.id.btn_ubah)

        btnUbah.setOnClickListener {
            // Tambahkan logika untuk tombol "Ubah" di sini
            val intent = Intent(this, MenuMerchantFragment::class.java)
            startActivity(intent)
        }

    }
}