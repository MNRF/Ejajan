package com.mnrf.ejajan.view.main.parent.ui.topup

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.CreditCardTokenRequestBuilder.Companion.CLIENT_KEY
import com.midtrans.sdk.uikit.external.UiKitApi
import com.mnrf.ejajan.R

class TransactionActivity : AppCompatActivity() {
    /*val CLIENT_KEY = "SB-Mid-client-STcLf66h68-oCykv"
    val BASE_URL = "https://your-server.com/transaction-finish"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_transaction)
        UiKitApi.Builder()
            .withMerchantClientKey(CLIENT_KEY) // client_key is mandatory
            .withContext(this) // context is mandatory
            .withMerchantUrl(BASE_URL) // set transaction finish callback (sdk callback)
            .enableLog(true) // enable sdk log (optional)
            .withFontFamily(ASSET_FONT)
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()
        setLocaleNew("en")
    }
    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }*/
}