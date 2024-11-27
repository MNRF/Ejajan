package com.mnrf.ejajan.view.main.parent.ui.setting.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R

class ProfileParentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_profile)

        val btnSave = findViewById<Button>(R.id.btn_save)

        btnSave.setOnClickListener {
            // Tambahkan logika untuk tombol Edit Profile
            val intent = Intent(this, ProfileParentActivity::class.java)
            startActivity(intent)
        }

    }
}