package com.mnrf.ejajan.view.main.merchant.ui.menu.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R
import com.mnrf.ejajan.view.main.merchant.ui.menu.MenuMerchantFragment
import com.mnrf.ejajan.view.main.merchant.ui.menu.change.UpdateMenuActivity

class DetailMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_detail_menu)

        val btnUbah = findViewById<Button>(R.id.btn_update)
        val btnHapus = findViewById<Button>(R.id.btn_delete)

        btnUbah.setOnClickListener {
            // Tambahkan logika untuk tombol "Ubah" di sini
            val intent = Intent(this, UpdateMenuActivity::class.java)
            startActivity(intent)
        }

        btnHapus.setOnClickListener {
            // Tambahkan logika untuk tombol "Hapus" di sini
            val intent = Intent(this, MenuMerchantFragment::class.java)
            startActivity(intent)
        }
    }
}