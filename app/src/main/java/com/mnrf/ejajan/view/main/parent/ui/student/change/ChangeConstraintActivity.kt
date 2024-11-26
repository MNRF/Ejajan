package com.mnrf.ejajan.view.main.parent.ui.student.change

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R
import com.mnrf.ejajan.view.main.parent.ui.student.add.AddConstraintActivity

class ChangeConstraintActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_change)

        val btnUbah = findViewById<Button>(R.id.btn_change)

        btnUbah.setOnClickListener {
            val intent = Intent(this, ChangeConstraintActivity::class.java)
            startActivity(intent)
        }
    }
}