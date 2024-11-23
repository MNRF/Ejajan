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

class LoginParentMerchant : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_parent_merchant)

        supportActionBar?.hide()

        val emailEditText = findViewById<EditText>(R.id.ed_login_email)
        val passwordEditText = findViewById<EditText>(R.id.ed_login_password)
        val loginButton = findViewById<Button>(R.id.buttonLoginParentMerchant)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when {
                email.contains("parent") -> {
                    val intent = Intent(this, ParentActivity::class.java)
                    startActivity(intent)
                }
                email.contains("merchant") -> {
                    val intent = Intent(this, MerchantActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, getString(R.string.invalid_role), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
