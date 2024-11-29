package com.mnrf.ejajan.view.main.parent.ui.topup

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.external.UiKitApi
import com.mnrf.ejajan.R

class PaymentMidtrans : AppCompatActivity() {
    private val clientKey = "SB-Mid-client-STcLf66h68-oCykv"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_transaction)

        try {
            Log.d("TransactionActivity", "Menginisialisasi Midtrans SDK...")
            UiKitApi.Builder()
                .withMerchantClientKey(clientKey)
                .withContext(this)
                .enableLog(true)
                .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
                .build()
            Log.d("TransactionActivity", "Midtrans SDK berhasil diinisialisasi")
        } catch (e: Exception) {
            Log.e("TransactionActivity", "Gagal menginisialisasi Midtrans SDK: ${e.message}", e)
        }

        setLocaleNew("en")
    }

    @Suppress("SameParameterValue")
    private fun setLocaleNew(languageCode: String?) {
        try {
            Log.d("TransactionActivity", "Mengatur locale ke: $languageCode")
            val locales = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(locales)
        } catch (e: Exception) {
            Log.e("TransactionActivity", "Gagal mengatur locale: ${e.message}", e)
        }
    }
}
