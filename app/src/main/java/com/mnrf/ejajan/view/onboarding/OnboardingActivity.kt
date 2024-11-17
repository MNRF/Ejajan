package com.mnrf.ejajan.view.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mnrf.ejajan.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRoleStudent.setOnClickListener {
            navigateToViewPager("student")
        }

        binding.btnRoleParent.setOnClickListener {
            navigateToViewPager("parent")
        }

        binding.btnRoleMerchant.setOnClickListener {
            navigateToViewPager("merchant")
        }
    }

    private fun navigateToViewPager(role: String) {
        val intent = Intent(this, OnboardingViewPager::class.java)
        intent.putExtra("ROLE_KEY", role)
        startActivity(intent)
    }
}
