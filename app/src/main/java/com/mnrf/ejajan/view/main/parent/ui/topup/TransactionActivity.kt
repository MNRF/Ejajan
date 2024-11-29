package com.mnrf.ejajan.view.main.parent.ui.topup

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mnrf.ejajan.R

class TransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_transaction)

        // Menghubungkan komponen UI
        val etAmount: EditText = findViewById(R.id.etAmount)
        val btnConfirm: Button = findViewById(R.id.btnConfirm)

        // Menambahkan logika untuk tombol Confirm
        btnConfirm.setOnClickListener {
            val amountText = etAmount.text.toString()
            if (amountText.isNotEmpty()) {
                val amount = amountText.toDoubleOrNull()
                if (amount != null && amount > 0) {
                    Toast.makeText(this, "Top Up Amount: Rp $amount", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
