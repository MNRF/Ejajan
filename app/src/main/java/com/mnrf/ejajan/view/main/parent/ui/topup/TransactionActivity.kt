package com.mnrf.ejajan.view.main.parent.ui.topup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_PENDING
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_SUCCESS
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_FAILED
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_INVALID
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CANCELED
import com.mnrf.ejajan.databinding.ActivityParentTransactionBinding
import com.mnrf.ejajan.view.utils.ViewModelFactory

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentTransactionBinding
    private val topUpViewModel: TopUpViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private val handler = Handler(Looper.getMainLooper())
    private var pollingRunnable: Runnable? = null
    private var amount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnConfirm.setOnClickListener {
            val amountText = binding.etAmount.text.toString()
            amount = amountText.toDoubleOrNull() ?: 0.0

            if (amount >= 5000) {
                topUpViewModel.startTransaction(amount)
            } else {
                Toast.makeText(this, "Enter a valid amount (min Rp5,000)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        topUpViewModel.transactionResponse.observe(this) { response ->
            if (response != null) {
                response.token?.let { startUiKit(it) }
            }
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
            .withMerchantUrl("https://capstone-c242-ps370.et.r.appspot.com/")
            .enableLog(true)
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()

        UiKitApi.getDefaultInstance().startPaymentUiFlow(this, launcher, snapToken)
    }

    /*private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val transactionResult = result.data?.getParcelableExtra<TransactionResult>(
                    UiKitConstants.KEY_TRANSACTION_RESULT
                )
                handleTransactionResult(transactionResult)
            } else {
                Toast.makeText(this, "Transaction Canceled or Failed.", Toast.LENGTH_SHORT).show()
            }
        }*/

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result?.resultCode == RESULT_OK) {
            result.data?.let {
                val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                Toast.makeText(this,"${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val transactionResult = data?.getParcelableExtra<TransactionResult>(
                UiKitConstants.KEY_TRANSACTION_RESULT
            )
            if (transactionResult != null) {
                when (transactionResult.status) {
                    STATUS_SUCCESS -> {
                        Toast.makeText(this, "Transaction Finished. ID: ${transactionResult.transactionId}", Toast.LENGTH_LONG).show()
                        navigateToConfirmationActivity()
                    }
                    STATUS_PENDING -> {
                        Toast.makeText(this, "Transaction Pending. Starting background checks...", Toast.LENGTH_LONG).show()
                        topUpViewModel.transactionResponse.observe(this) { response ->
                            if (response != null) {
                                startPolling(response.token)
                            }
                        }
                    }
                    STATUS_FAILED -> {
                        Toast.makeText(this, "Transaction Failed. ID: ${transactionResult.transactionId}", Toast.LENGTH_LONG).show()
                    }
                    STATUS_CANCELED -> {
                        Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG).show()
                    }
                    STATUS_INVALID -> {
                        Toast.makeText(this, "Transaction Invalid. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this, "Transaction ID: " + transactionResult.transactionId + ". Message: " + transactionResult.status, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /*private fun handleTransactionResult(transactionResult: TransactionResult?) {
        transactionResult?.let {
            when (it.status) {
                STATUS_SUCCESS -> {
                    Toast.makeText(this, "Transaction Finished. ID: ${it.transactionId}", Toast.LENGTH_LONG).show()
                    navigateToConfirmationActivity()
                }
                STATUS_PENDING -> {
                    Toast.makeText(this, "Transaction Pending. Starting background checks...", Toast.LENGTH_LONG).show()
                    startPolling(it.transactionId)
                }
                STATUS_FAILED -> {
                    Toast.makeText(this, "Transaction Failed. ID: ${it.transactionId}", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "Unknown status: ${it.status}", Toast.LENGTH_LONG).show()
                }
            }
        } ?: run {
            Toast.makeText(this, "Transaction Result is null", Toast.LENGTH_LONG).show()
        }
    }*/

    private fun startPolling(transactionId: String?) {
        if (transactionId == null) {
            Toast.makeText(this, "Transaction ID is null", Toast.LENGTH_SHORT).show()
            return
        }

        println("Starting polling for transaction ID: $transactionId")
        pollingRunnable = object : Runnable {
            override fun run() {
                topUpViewModel.checkTransactionStatus(transactionId) { status ->
                    println("QWERTY2 $status")
                    handler.post {
                        when (status) {
                            "settlement" -> {
                                Toast.makeText(this@TransactionActivity, "Transaction Successful", Toast.LENGTH_SHORT).show()
                                stopPolling()
                                navigateToConfirmationActivity()
                            }
                            "pending" -> {
                                Toast.makeText(this@TransactionActivity, "Transaction Pending", Toast.LENGTH_SHORT).show()
                                handler.postDelayed(this, POLLING_INTERVAL)
                            }
                            "deny", "cancel", "expire" -> {
                                Toast.makeText(this@TransactionActivity, "Transaction Failed or Cancelled", Toast.LENGTH_SHORT).show()
                                stopPolling()
                            }
                            null -> {
                                Toast.makeText(this@TransactionActivity, "Error fetching transaction status", Toast.LENGTH_SHORT).show()
                                stopPolling()
                            }
                            else -> {
                                Toast.makeText(this@TransactionActivity, "Unknown transaction status: $status", Toast.LENGTH_SHORT).show()
                                stopPolling()
                            }
                        }
                    }
                }
            }
        }

        handler.post(pollingRunnable!!)
    }




    private fun stopPolling() {
        pollingRunnable?.let { handler.removeCallbacks(it) }
        pollingRunnable = null
    }

    private fun navigateToConfirmationActivity() {
        val intent = Intent(this, ConfirmationActivity::class.java)
        intent.putExtra(ConfirmationActivity.TRANSACTION_SUCCESS, amount.toInt().toString())
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPolling()
    }

    companion object {
        private const val POLLING_INTERVAL = 5000L // 5 seconds
    }
}