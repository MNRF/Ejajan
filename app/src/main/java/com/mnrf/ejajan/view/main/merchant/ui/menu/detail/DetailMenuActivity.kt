package com.mnrf.ejajan.view.main.merchant.ui.menu.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mnrf.ejajan.databinding.ActivityMerchantDetailMenuBinding
import com.mnrf.ejajan.view.main.merchant.MerchantActivity
import com.mnrf.ejajan.view.main.merchant.ui.menu.MenuMerchantFragment
import com.mnrf.ejajan.view.main.merchant.ui.menu.change.UpdateMenuActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory

class DetailMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMerchantDetailMenuBinding

    private val detailMenuViewModel: DetailMenuViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMerchantDetailMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etNamefood.setText(intent.getStringExtra(MENU_NAME))
        binding.etDescription.setText(intent.getStringExtra(MENU_DESCRIPTION))
        binding.etBasicIngredients.setText(intent.getStringExtra(MENU_INGREDIENTS))
        binding.etPreparation.setText(intent.getStringExtra(MENU_PREPARATIONTIME))
        binding.etPricefood.setText(intent.getStringExtra(MENU_PRICE))
        val menuId = intent.getStringExtra(MENU_ID).toString()

        binding.btnUpdate.setOnClickListener {
            val name = binding.etNamefood.text.toString()
            val description = binding.etDescription.text.toString()
            val ingredients = binding.etBasicIngredients.text.toString()
            val preparationtime = binding.etPreparation.text.toString()
            val price = binding.etPricefood.text.toString()
            detailMenuViewModel.updateMenu(menuId, name, description, ingredients, preparationtime, price)

            val intent = Intent(this, MerchantActivity::class.java)
            startActivity(intent)
        }

       binding.btnDelete.setOnClickListener {
            detailMenuViewModel.deleteMenu(menuId)
            val intent = Intent(this, MerchantActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val MENU_ID = "menu_id"
        const val MENU_NAME = "menu_name"
        const val MENU_DESCRIPTION = "menu_description"
        const val MENU_INGREDIENTS = "menu_ingredients"
        const val MENU_PREPARATIONTIME = "menu_preparationtime"
        const val MENU_PRICE = "menu_price"
    }
}