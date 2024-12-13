package com.mnrf.ejajan.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.databinding.ActivityLoginParentMerchantBinding
import com.mnrf.ejajan.view.main.parent.ParentActivity
import com.mnrf.ejajan.view.main.merchant.MerchantActivity
import com.mnrf.ejajan.data.pref.OnboardingPreferences
import com.mnrf.ejajan.view.main.admin.RegisterActivity
import com.mnrf.ejajan.view.onboarding.OnboardingActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory

class LoginParentMerchant : AppCompatActivity() {
    private lateinit var binding: ActivityLoginParentMerchantBinding
    private  var userRole: String? = null
    private var currentUser: FirebaseUser? = null

    private val loginParentMerchantViewModel: LoginParentMerchantViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onStart() {
        super.onStart()

        // Periksa apakah ada pengguna yang sudah login
        currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val onboardingPreferences = OnboardingPreferences(this)
            userRole = onboardingPreferences.getUserRole()

            if (!userRole.isNullOrBlank()) {
                navigateToRoleActivity()
            } else {
                showAlert("Role tidak ditemukan, silakan login ulang.") {
                    Firebase.auth.signOut()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginParentMerchantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Ambil role pengguna dari OnboardingPreferences

        // Observasi sesi login untuk menavigasi pengguna
        observeSession()

        // Observe loading state for progress bar
        loginParentMerchantViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.buttonLoginParentMerchant.setOnClickListener {
            handleLogin()
        }
    }

    private fun observeSession() {
        loginParentMerchantViewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                val onboardingPreferences = OnboardingPreferences(this)
                userRole = onboardingPreferences.getUserRole().toString()
                navigateToRoleActivity()
            }
        }
    }

    private fun handleLogin() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        if (email.isBlank() || password.isBlank()) {
            showAlert("Email dan password tidak boleh kosong.")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showAlert("Format email tidak valid.")
            return
        }

        if (password.length < 8) {
            showAlert("Password minimal 8 karakter.")
            return
        }

        userRole = when {
            email.endsWith("@parent.ejajan.com") -> "parent"
            email.endsWith("@merchant.ejajan.com") -> "merchant"
            email.endsWith("@admin.ejajan.com") -> "admin"
            else -> null
        }

        if (userRole == null) {
            showAlert("Email tidak sesuai dengan role yang valid.")
            return
        }

        loginParentMerchantViewModel.login(email, password)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoginparentmerchant.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToRoleActivity() {
        val intent = when (userRole) {
            "parent" -> Intent(this, ParentActivity::class.java)
            "merchant" -> Intent(this, MerchantActivity::class.java)
            "admin" -> Intent(this, RegisterActivity::class.java)
            else -> {
                showAlert("Role tidak ditemukan!")
                return
            }
        }
        startActivity(intent)
        finish()
    }

    private fun showAlert(message: String, onDismiss: (() -> Unit)? = null) {
        if (isFinishing) {
            Log.e(TAG, "Activity is finishing, cannot show alert.")
            return
        }

        Log.d(TAG, "showAlert dipanggil dengan pesan: $message")
        AlertDialog.Builder(this).apply {
            setTitle("Informasi")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
                Log.d(TAG, "Dialog ditutup, memanggil onDismiss.")
                onDismiss?.invoke()
            }
            create()
            show()
        }
    }


    companion object {
        private const val TAG = "EmailPassword"
    }
}