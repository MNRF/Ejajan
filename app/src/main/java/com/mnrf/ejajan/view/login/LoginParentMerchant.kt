package com.mnrf.ejajan.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mnrf.ejajan.R
import com.mnrf.ejajan.view.main.parent.ParentActivity
import com.mnrf.ejajan.view.main.merchant.MerchantActivity
import com.mnrf.ejajan.view.main.student.StudentActivity
import com.mnrf.ejajan.view.utils.OnboardingPreferences

class LoginParentMerchant : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_parent_merchant)

        supportActionBar?.hide()

        val onboardingPreferences = OnboardingPreferences(this)
        val userRole = onboardingPreferences.getUserRole()

        val loginButton = findViewById<Button>(R.id.buttonLoginParentMerchant)

        loginButton.setOnClickListener {
            when (userRole) {
                "parent" -> {
                    val intent = Intent(this, ParentActivity::class.java)
                    startActivity(intent)
                }
                "merchant" -> {
                    val intent = Intent(this, MerchantActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, "Role tidak ditemukan!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

