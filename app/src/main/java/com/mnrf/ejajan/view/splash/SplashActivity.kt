package com.mnrf.ejajan.view.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.mnrf.ejajan.R
import com.mnrf.ejajan.view.login.LoginParentMerchant
import com.mnrf.ejajan.view.login.LoginStudent
import com.mnrf.ejajan.view.onboarding.OnboardingActivity
import com.mnrf.ejajan.view.utils.OnboardingPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var onboardingPreferences: OnboardingPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        onboardingPreferences = OnboardingPreferences(this)

        lifecycleScope.launch {
            delay(1000)

            if (onboardingPreferences.isOnboardingCompleted()) {
                when (onboardingPreferences.getUserRole()) {
                    "parent", "merchant" -> {
                        startActivity(Intent(this@SplashActivity, LoginParentMerchant::class.java))
                    }
                    "student" -> {
                        startActivity(Intent(this@SplashActivity, LoginStudent::class.java))
                    }
                    else -> {
                        startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                    }
                }
            } else {
                startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
            }
            finish()
        }
    }
}