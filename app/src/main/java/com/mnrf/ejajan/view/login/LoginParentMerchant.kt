package com.mnrf.ejajan.view.login

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.mnrf.ejajan.R

class LoginParentMerchant : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_parent_merchant)

        supportActionBar?.hide()

        onBackPressedDispatcher.addCallback(this) {
            finishAffinity()
        }
    }
}