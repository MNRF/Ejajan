package com.mnrf.ejajan.view.main.parent.ui.student.delete

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R

class DeleteConstraintActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_delete)

        val btnHapus = findViewById<Button>(R.id.btn_delete)

        btnHapus.setOnClickListener {
            val intent = Intent(this, DeleteConstraintActivity::class.java)
            startActivity(intent)
        }
    }
}