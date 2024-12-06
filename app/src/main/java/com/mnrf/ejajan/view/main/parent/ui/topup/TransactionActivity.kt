package com.mnrf.ejajan.view.main.parent.ui.topup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.mnrf.ejajan.databinding.ActivityParentTransactionBinding

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentTransactionBinding

    // Define the ActivityResultLauncher at the class level so it can be used in multiple methods
    private val transactionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val transactionResult = result.data?.getParcelableExtra<TransactionResult>(
                    UiKitConstants.KEY_TRANSACTION_RESULT
                )
                if (transactionResult != null) {
                    when (transactionResult.status) {
                        "SUCCESS" -> {
                            Toast.makeText(this, "Transaction Success. ID: ${transactionResult.transactionId}", Toast.LENGTH_LONG).show()
                        }
                        "PENDING" -> {
                            Toast.makeText(this, "Transaction Pending. ID: ${transactionResult.transactionId}", Toast.LENGTH_LONG).show()
                        }
                        "FAILED" -> {
                            Toast.makeText(this, "Transaction Failed. ID: ${transactionResult.transactionId}", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this, "Transaction Status Unknown", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check permission for READ_PHONE_STATE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                101
            )
        }

        // Button click listener to trigger the transaction process
        binding.btnConfirm.setOnClickListener {
            val amountText = binding.etAmount.text.toString()
            /*if (amountText.isNotEmpty()) {
                val amount = amountText.toDoubleOrNull()
                if (amount != null && amount > 10000) {
                    Toast.makeText(this, "Top Up Amount: Rp $amount", Toast.LENGTH_SHORT).show()
                    startTransaction()  // Proceed with the transaction
                } else {
                    Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show()
            }*/
            intent = Intent(this, ConfirmationActivity::class.java)
            intent.putExtra(ConfirmationActivity.TRANSACTION_SUCCESS, amountText)
            startActivity(intent)
            finish()
        }
    }

    private fun startTransaction() {
        // Fetch the Snap Token from your backend (this should be done via an API call)
        // For now, we use a mock token. In your production app, make sure to fetch the token from the backend.
        val snapToken = "25e3659b-f00c-4d98-bfff-9f72978c8df5"

        // Initialize UiKitApi with your credentials and configuration
        UiKitApi.Builder()
            .withMerchantClientKey(CLIENT_KEY)
            .withContext(this)
            .withMerchantUrl(BASE_URL)
            .enableLog(true)
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()

        // Now start the payment flow with the provided snapToken
        UiKitApi.getDefaultInstance().startPaymentUiFlow(this, transactionLauncher, snapToken)

        // Set locale (optional)
        setLocaleNew("en")
    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    companion object {
        const val CLIENT_KEY = "SB-Mid-client-STcLf66h68-oCykv"
        const val BASE_URL = "https://midtrans-api-934253159531.asia-southeast2.run.app/"
    }
}
