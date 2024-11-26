package com.mnrf.ejajan.view.main.merchant.ui.menu.add

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R

class AddIngredientsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_add_ingredients)

        val btnSimpan = findViewById<Button>(R.id.btn_simpan)
        val btnBatal = findViewById<Button>(R.id.btn_batal)

        btnSimpan.setOnClickListener {
            // Tambahkan logika untuk tombol "Simpan" di sini
            val intent = Intent(this, AddIngredientsActivity::class.java)
            startActivity(intent)
        }

        btnBatal.setOnClickListener {
            // Tambahkan logika untuk tombol "Batal" di sini
            val intent = Intent(this, AddMenuActivity::class.java)
            startActivity(intent)
        }
    }
}