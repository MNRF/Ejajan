package com.mnrf.ejajan.view.main.merchant.ui.menu.add

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.ActivityMerchantAddMenuBinding
import com.mnrf.ejajan.databinding.ActivityParentBinding
import com.mnrf.ejajan.view.login.LoginParentMerchantViewModel
import com.mnrf.ejajan.view.main.merchant.MerchantActivity
import com.mnrf.ejajan.view.main.merchant.ui.menu.MenuMerchantFragment
import com.mnrf.ejajan.view.utils.ViewModelFactory

class AddMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMerchantAddMenuBinding

    private val merchantAddMenuViewModel: MerchantAddMenuViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var isMenuSubmitted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMerchantAddMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTambah.setOnClickListener {
            if (isMenuSubmitted) return@setOnClickListener // Prevent multiple submissions

            merchantAddMenuViewModel.getSession().observe(this) { user ->
                val merchantUid = user.token

                if (!isMenuSubmitted && merchantUid.isNotEmpty()) {
                    isMenuSubmitted = true
                    merchantAddMenuViewModel.addMenu(
                        merchantUid,
                        binding.etFoodName.text.toString(),
                        binding.etDescription.text.toString(),
                        binding.etBasicIngredients.text.toString(),
                        binding.etAddPreparetime.text.toString()
                    )
                    val intent = Intent(this, MerchantActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

}