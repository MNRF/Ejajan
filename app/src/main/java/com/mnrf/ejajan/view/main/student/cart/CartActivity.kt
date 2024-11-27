package com.mnrf.ejajan.view.main.student.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R
import com.mnrf.ejajan.view.main.student.detail.DetailMenuStudentActivity
import com.mnrf.ejajan.view.main.student.face.FaceConfirmActivity
import com.mnrf.ejajan.view.main.student.personal.PersonalPicksActivity

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_cart)

        findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            startActivity(Intent(this, DetailMenuStudentActivity::class.java))
        }

        findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            startActivity(Intent(this, FaceConfirmActivity::class.java))
        }

    }
}