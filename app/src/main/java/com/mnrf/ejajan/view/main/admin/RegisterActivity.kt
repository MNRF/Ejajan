package com.mnrf.ejajan.view.main.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.pref.OnboardingPreferences
import com.mnrf.ejajan.databinding.ActivityLoginParentMerchantBinding
import com.mnrf.ejajan.view.login.LoginParentMerchantViewModel
import com.mnrf.ejajan.view.main.merchant.MerchantActivity
import com.mnrf.ejajan.view.main.parent.ParentActivity
import com.mnrf.ejajan.view.onboarding.OnboardingActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginParentMerchantBinding
    private  var userRole: String? = null
    private var currentUser: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth

    private val loginParentMerchantViewModel: LoginParentMerchantViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

/*    override fun onStart() {
        super.onStart()

        // Periksa apakah ada pengguna yang sudah login
        currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            navigateToRoleActivity()
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginParentMerchantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Ambil role pengguna dari OnboardingPreferences
        val onboardingPreferences = OnboardingPreferences(this)
        userRole = onboardingPreferences.getUserRole().toString()

        val name = binding.edLoginEmail.text.toString().replace(" ", "")
        val email = "${name}@${userRole}.ejajan.com"
        val password = "${name}@${userRole}.ejajan.com"

        createNewAccount(email, password)

/*        // Observasi sesi login untuk menavigasi pengguna
        observeSession()*/

        // Observe loading state for progress bar
/*        loginParentMerchantViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.buttonLoginParentMerchant.setOnClickListener {
            handleLogin()
        }*/
    }

    private fun createNewAccount(email: String, password: String) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    /*val user = auth.currentUser
                    updateUI(user)*/
                    startActivity(Intent(this, OnboardingActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    /*updateUI(null)*/
                }
            }
    }

/*    private fun observeSession() {
        loginParentMerchantViewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                navigateToRoleActivity()
            }
        }
    }*/

/*    private fun handleLogin() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        if (email.isBlank() || password.isBlank() ||
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.length < 8
        ) {
            showAlert("Isi dengan lengkap dan benar!")
            return
        }

        // Call the ViewModel's login function
        loginParentMerchantViewModel.login(email, password)
        if ("@parent.ejajan.com" in email) {
            userRole = "parent"
        } else if ("@merchant.ejajan.com" in email) {
            userRole = "merchant"
        }else if ("@admin.ejajan.com" in email) {
            userRole = "admin"
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoginparentmerchant.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToRoleActivity() {
        val intent = when (userRole) {
            "parent" -> Intent(this, ParentActivity::class.java)
            "merchant" -> Intent(this, MerchantActivity::class.java)
            "admin" -> Intent(this, OnboardingActivity::class.java)
            else -> {
                showAlert("Role tidak ditemukan!")
                return
            }
        }
        startActivity(intent)
        finish()
    }*/

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