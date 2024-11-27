package com.mnrf.ejajan.view.main.parent.ui.student.add

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R

class AddConstraintActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_add)

        val btnTambah = findViewById<Button>(R.id.btn_create)

        btnTambah.setOnClickListener {
            val intent = Intent(this, AddConstraintActivity::class.java)
            startActivity(intent)
        }
    }
}