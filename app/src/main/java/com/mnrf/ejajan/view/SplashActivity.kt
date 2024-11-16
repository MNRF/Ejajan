package com.mnrf.ejajan.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mnrf.ejajan.R
import com.mnrf.ejajan.view.login.LoginStudent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        lifecycleScope.launch {
            delay(3000)
            val intent = Intent(this@SplashActivity, LoginStudent::class.java)
            startActivity(intent)
            finish()
        }
    }
}