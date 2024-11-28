package com.mnrf.ejajan.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.pref.UserModel
import com.mnrf.ejajan.databinding.ActivityLoginParentMerchantBinding
import com.mnrf.ejajan.view.main.parent.ParentActivity
import com.mnrf.ejajan.view.main.merchant.MerchantActivity
import com.mnrf.ejajan.view.utils.OnboardingPreferences
import com.mnrf.ejajan.view.utils.ViewModelFactory

class LoginParentMerchant : AppCompatActivity() {
    private lateinit var binding: ActivityLoginParentMerchantBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userRole: String

    private val loginParentMerchantViewModel: LoginParentMerchantViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth

        // Periksa apakah ada pengguna yang sudah login
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToRoleActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginParentMerchantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Ambil role pengguna dari OnboardingPreferences
        val onboardingPreferences = OnboardingPreferences(this)
        userRole = onboardingPreferences.getUserRole().toString()

        // Observasi sesi login untuk menavigasi pengguna
        observeSession()

        binding.buttonLoginParentMerchant.setOnClickListener {
            handleLogin()
        }

    }

    private fun observeSession() {
        loginParentMerchantViewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                navigateToRoleActivity()
            }
        }
    }

    private fun handleLogin() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        if (email.isBlank() || password.isBlank() ||
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.length < 8
        ) {
            showAlert("Isi dengan lengkap dan benar!")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    saveUserSession(user)
                    navigateToRoleActivity()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    showAlert("Login gagal. Pastikan email dan password sudah benar.")
                }
            }
    }

    private fun saveUserSession(user: FirebaseUser?) {
        user?.let {
            val email = it.email ?: ""
            val uid = it.uid
            val userModel = UserModel(email, uid, true)
            loginParentMerchantViewModel.saveSession(userModel)
        }
    }

    private fun navigateToRoleActivity() {
        val intent = when (userRole) {
            "parent" -> Intent(this, ParentActivity::class.java)
            "merchant" -> Intent(this, MerchantActivity::class.java)
            else -> {
                showAlert("Role tidak ditemukan!")
                return
            }
        }
        startActivity(intent)
        finish()
    }

    private fun showAlert(message: String, onDismiss: (() -> Unit)? = null) {
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


