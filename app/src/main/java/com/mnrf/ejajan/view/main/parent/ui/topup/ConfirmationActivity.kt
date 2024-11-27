package com.mnrf.ejajan.view.main.parent.ui.topup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.FragmentMerchantHomeBinding

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_confirmation)

        val btnBack = findViewById<Button>(R.id.btn_back_to_home)

        btnBack.setOnClickListener {
            val intent = Intent(this, FragmentMerchantHomeBinding::class.java)
            startActivity(intent)
        }
    }
}