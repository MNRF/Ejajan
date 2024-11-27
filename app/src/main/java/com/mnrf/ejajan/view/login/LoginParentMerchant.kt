package com.mnrf.ejajan.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.pref.UserModel
import com.mnrf.ejajan.databinding.ActivityLoginParentMerchantBinding
import com.mnrf.ejajan.view.main.parent.ParentActivity
import com.mnrf.ejajan.view.main.merchant.MerchantActivity
import com.mnrf.ejajan.view.main.student.StudentActivity
import com.mnrf.ejajan.view.utils.OnboardingPreferences
import com.mnrf.ejajan.view.utils.ViewModelFactory

class LoginParentMerchant : AppCompatActivity() {
    private lateinit var binding: ActivityLoginParentMerchantBinding
    private lateinit var auth: FirebaseAuth

    private val loginParentMerchantViewModel: LoginParentMerchantViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginParentMerchantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val onboardingPreferences = OnboardingPreferences(this)
        val userRole = onboardingPreferences.getUserRole()

        when (userRole) {
            "parent" -> {
                loginParentMerchantViewModel.getSession().observe(this) { user ->
                    if (!user.isLogin) {
                        val intent = Intent(this, ParentActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            "merchant" -> {
                loginParentMerchantViewModel.getSession().observe(this) { user ->
                    if (!user.isLogin) {
                        val intent = Intent(this, MerchantActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            /*else -> {
                Toast.makeText(this, "Role tidak ditemukan!", Toast.LENGTH_SHORT).show()
            }*/
        }

        val loginButton = findViewById<Button>(R.id.buttonLoginParentMerchant)

        auth = Firebase.auth

        loginButton.setOnClickListener {
            val emailEdt = binding.edLoginEmail.text.toString()
            val passwordEdt = binding.edLoginPassword.text.toString()

            if (emailEdt.isBlank() || passwordEdt.isBlank()
                || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdt).matches()
                || passwordEdt.length < 8) {
                showAlert("Isi dengan lengkap dan benar!")
                return@setOnClickListener
            }

            when (userRole) {
                "parent" -> {
                    auth.signInWithEmailAndPassword(emailEdt, passwordEdt)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user)
                                val userFirebase = Firebase.auth.currentUser
                                userFirebase?.let {
                                    val email = it.email
                                    val uid = it.uid
                                    email?.let { it1 ->
                                        UserModel(
                                            it1, uid)
                                    }?.let { it2 -> loginParentMerchantViewModel.saveSession(it2) }
                                }
                                val intent = Intent(this, ParentActivity::class.java)
                                startActivity(intent)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                updateUI(null)
                            }
                        }
                }
                "merchant" -> {
                    auth.signInWithEmailAndPassword(emailEdt, passwordEdt)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user)
                                val userFirebase = Firebase.auth.currentUser
                                userFirebase?.let {
                                    val email = it.email
                                    val uid = it.uid
                                    email?.let { it1 ->
                                        UserModel(
                                            it1, uid)
                                    }?.let { it2 -> loginParentMerchantViewModel.saveSession(it2) }
                                }
                                val intent = Intent(this, MerchantActivity::class.java)
                                startActivity(intent)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                updateUI(null)
                            }
                        }
                }
                else -> {
                    Toast.makeText(this, "Role tidak ditemukan!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
    }


    private fun reload() {
    }

    private fun showAlert(message: String, onDismiss: (() -> Unit)? = null) {
        AlertDialog.Builder(this).apply {
            setTitle("Informasi")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ -> onDismiss?.invoke() }
            create()
            show()
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}

