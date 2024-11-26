package com.mnrf.ejajan.view.main.student.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R
import com.mnrf.ejajan.view.main.student.cart.CartActivity
import com.mnrf.ejajan.view.main.student.personal.PersonalPicksActivity

class DetailMenuStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_detail_menu)

        findViewById<Button>(R.id.action_button).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}