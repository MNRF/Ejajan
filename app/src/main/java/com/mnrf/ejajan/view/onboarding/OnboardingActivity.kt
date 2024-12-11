package com.mnrf.ejajan.view.onboarding

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mnrf.ejajan.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        playAnimation()

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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.choseeRole, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val welcome = ObjectAnimator.ofFloat(binding.welcomeText, View.ALPHA, 1f).setDuration(100)
        val role = ObjectAnimator.ofFloat(binding.roleText, View.ALPHA, 1f).setDuration(100)
        val btnStudent = ObjectAnimator.ofFloat(binding.btnRoleStudent, View.ALPHA, 1f).setDuration(100)
        val btnParent = ObjectAnimator.ofFloat(binding.btnRoleParent, View.ALPHA, 1f).setDuration(100)
        val btnMerchant = ObjectAnimator.ofFloat(binding.btnRoleMerchant, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(welcome, role, btnStudent, btnParent, btnMerchant)
            start()
        }
    }
}
