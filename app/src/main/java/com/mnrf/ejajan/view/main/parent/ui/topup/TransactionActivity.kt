package com.mnrf.ejajan.view.main.parent.ui.topup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.mnrf.ejajan.databinding.ActivityParentTransactionBinding
import com.mnrf.ejajan.view.main.parent.ParentActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentTransactionBinding

    private val topUpViewModel: TopUpViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    // ActivityResultLauncher untuk menangani hasil transaksi dari Midtrans UI Kit
    private val transactionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val transactionResult = result.data?.getParcelableExtra<TransactionResult>(
                    UiKitConstants.KEY_TRANSACTION_RESULT
                )
                handleTransactionResult(transactionResult)
            } else {
                Toast.makeText(this, "Transaction Canceled or Failed.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Transaction Top Up"
        }

        binding.btnConfirm.setOnClickListener {
            val amountText = binding.etAmount.text.toString()
            val amount = amountText.toDoubleOrNull()

            if (amount != null && amount > 0) {
                topUpViewModel.startTransaction(amount)
            } else {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        topUpViewModel.transactionResponse.observe(this) { response ->
            if (response != null) {
                // Start Midtrans UI Kit with the Snap Token
                startUiKit(response.token)
            }
        }

        topUpViewModel.isLoading.observe(this) { isLoading ->
            binding.btnConfirm.isEnabled = !isLoading
        }

        topUpViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startUiKit(snapToken: String) {
        UiKitApi.Builder()
            .withMerchantClientKey("SB-Mid-client-STcLf66h68-oCykv")
            .withContext(this)
            .withMerchantUrl("https://capstone-c242-ps370.et.r.appspot.com/") // Ganti dengan URL backend Anda
            .enableLog(true)
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()

        UiKitApi.getDefaultInstance().startPaymentUiFlow(this, transactionLauncher, snapToken)
    }

    private fun handleTransactionResult(transactionResult: TransactionResult?) {
        if (transactionResult != null) {
            Log.d("TransactionResult", "Status: ${transactionResult.status}")
            Log.d("TransactionResult", "Transaction ID: ${transactionResult.transactionId}")
            Log.d("TransactionResult", "Message: ${transactionResult.message}")

            when (transactionResult.status) {
                "SUCCESS" -> {
                    Toast.makeText(this, "Transaction Successful!", Toast.LENGTH_LONG).show()
                    updateUI("SUCCESS", transactionResult.transactionId)
                    navigateToParentActivity(transactionResult.transactionId)
                    updateBalance(transactionResult.transactionId ?: "")
                }
                "PENDING" -> {
                    Toast.makeText(this, "Transaction Pending.", Toast.LENGTH_LONG).show()
                    updateUI("PENDING", transactionResult.transactionId)
                }
                "FAILED" -> {
                    Toast.makeText(this, "Transaction Failed.", Toast.LENGTH_LONG).show()
                    updateUI("FAILED", transactionResult.transactionId)
                }
                "INVALID" -> {
                    Toast.makeText(this, "Transaction Invalid.", Toast.LENGTH_LONG).show()
                    updateUI("INVALID", null)
                }
                else -> {
                    Toast.makeText(this, "Transaction Status Unknown.", Toast.LENGTH_LONG).show()
                    updateUI("UNKNOWN", null)
                }
            }
        } else {
            Log.e("TransactionResult", "Transaction Result is Null.")
            Toast.makeText(this, "Transaction Result is Null.", Toast.LENGTH_LONG).show()
            updateUI("NULL", null)
        }
    }

    private fun updateUI(status: String, transactionId: String?) {
        binding.tvTransactionStatus.text = "Status: $status"
        if (transactionId != null) {
            binding.tvTransactionId.text = "Transaction ID: $transactionId"
        } else {
            binding.tvTransactionId.text = "Transaction ID: Not Available"
        }
        // Nonaktifkan tombol konfirmasi setelah transaksi selesai
        binding.btnConfirm.isEnabled = false
    }

    private fun updateBalance(transactionId: String) {
        // Kirim ke backend untuk memperbarui saldo
        // Pastikan backend Anda memiliki endpoint untuk menangani ini
        Log.d("UpdateBalance", "Transaction ID: $transactionId sent to backend.")
        // Contoh sederhana
        Toast.makeText(this, "Saldo berhasil diperbarui!", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToParentActivity(transactionId: String?) {
        val intent = Intent(this, ParentActivity::class.java).apply {
            putExtra("TRANSACTION_ID", transactionId)
        }
        startActivity(intent)
        finish() // Mengakhiri activity ini
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

//    private fun navigateToConfirmationActivity(status: String, transactionId: String?) {
//        val intent = Intent(this, ParentActivity::class.java).apply {
//            putExtra("TRANSACTION_STATUS", status)
//            putExtra("TRANSACTION_ID", transactionId)
//        }
//        startActivity(intent)
//        finish() // Mengakhiri activity ini
//    }
}
