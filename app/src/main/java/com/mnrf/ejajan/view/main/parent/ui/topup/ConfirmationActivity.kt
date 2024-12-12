package com.mnrf.ejajan.view.main.parent.ui.topup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.ActivityParentConfirmationBinding
import com.mnrf.ejajan.databinding.ActivityParentTransactionBinding
import com.mnrf.ejajan.databinding.FragmentMerchantHomeBinding
import com.mnrf.ejajan.view.main.merchant.ui.setting.SettingViewModel
import com.mnrf.ejajan.view.main.parent.ParentActivity
import com.mnrf.ejajan.view.main.parent.ui.home.HomeViewModel
import com.mnrf.ejajan.view.utils.ViewModelFactory

class ConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentConfirmationBinding
    private val topUpViewModel: TopUpViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val topUpAmount = intent.getStringExtra(TRANSACTION_SUCCESS)
        if (topUpAmount != null) {
            topUpViewModel.topUp(topUpAmount.toInt())
        }

        binding.btnBackToHome.setOnClickListener {
            val intent = Intent(this, ParentActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val TRANSACTION_SUCCESS = "transaction_success"
        const val TRANSACTION_FAILED = "transaction_failed"
    }
}