package com.mnrf.ejajan.view.main.student.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.model.CartModel
import com.mnrf.ejajan.data.model.MenuModel
import com.mnrf.ejajan.data.model.OrderSummaryModel
import com.mnrf.ejajan.data.pref.CartPreferences
import com.mnrf.ejajan.databinding.ActivityStudentDetailMenuSpecialBinding
import com.mnrf.ejajan.view.main.student.adapter.NotesAdapter
import com.mnrf.ejajan.view.main.student.cart.CartActivity
import com.mnrf.ejajan.view.main.student.detail.DetailMenuStudentActivity.Companion
import com.mnrf.ejajan.view.utils.ViewModelFactory
import java.text.NumberFormat
import java.util.Locale

class DetailMenuSpecialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailMenuSpecialBinding

    private val detailMenuViewModel: DetailMenuStudentViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: NotesAdapter

    private var currentImageUrl: String? = null
    private lateinit var menu: MenuModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailMenuSpecialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Detail Menu"
        }

        menu = MenuModel(
            id = intent.getStringExtra(MENU_ID).orEmpty(),
            name = intent.getStringExtra(MENU_NAME).orEmpty(),
            description = intent.getStringExtra(MENU_DESCRIPTION).orEmpty(),
            ingredients = intent.getStringExtra(MENU_INGREDIENTS).orEmpty(), // Ambil dari Intent
            preparationtime = intent.getStringExtra(MENU_PREPARATIONTIME).orEmpty(), // Ambil dari Intent
            price = intent.getStringExtra(MENU_PRICE).orEmpty(),
            imageurl = intent.getStringExtra(MENU_IMAGE).orEmpty()
        )

        currentImageUrl = intent.getStringExtra(MENU_IMAGE)
        Glide.with(binding.imgAbout.context)
            .load(currentImageUrl)
            .into(binding.imgAbout)

        binding.tvName.text = intent.getStringExtra(MENU_NAME)?.uppercase()

        /*val originalPrice = menu.price.toDoubleOrNull() ?: 0.0
        val discount = 10
        val discountedPrice = originalPrice - (originalPrice * discount / 100)*/

        val originalPrice = intent.getStringExtra(DetailMenuStudentActivity.MENU_PRICE)?.toDoubleOrNull() ?: 0.0
        val discount = 10
        val discountedPrice = originalPrice - (originalPrice * discount / 100)

        binding.tvOriginalPrice.apply {
            text = context.getString(R.string.original_price_format, originalPrice.toInt())
            setTextColor(0xFF999999.toInt())
            paintFlags = paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.tvDiscountPrice.apply {
            text = context.getString(R.string.discounted_price_format, discountedPrice.toInt())
            setTextColor(0xFFFF0000.toInt())
        }

//        val price = intent.getStringExtra(MENU_PRICE)?.toDoubleOrNull() ?: 0.0
//        val formattedPrice = NumberFormat.getNumberInstance(Locale("id", "ID")).format(price)
//        binding.tvOriginalPrice.text = getString(R.string.price_format, formattedPrice)
//
//        val priceDiscount = intent.getStringExtra(MENU_PRICE)?.toDoubleOrNull() ?: 0.0
//        val formattedPriceDiscount = NumberFormat.getNumberInstance(Locale("id", "ID")).format(priceDiscount)
//        binding.tvDiscountPrice.text = getString(R.string.price_format, formattedPriceDiscount)

        val pickupTime = intent.getStringExtra(MENU_PREPARATIONTIME)?.toIntOrNull() ?: 0
        binding.tvPickupTime.text = getString(R.string.pickup, pickupTime)

        val description = intent.getStringExtra(MENU_DESCRIPTION)
        binding.tvDescription.text = HtmlCompat.fromHtml(description ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
        intent.getStringExtra(MENU_ID).toString()

        adapter = NotesAdapter()
        binding.rvNotes.layoutManager = GridLayoutManager(this, 2)
        binding.rvNotes.adapter = adapter

        detailMenuViewModel.menuList.observe(this) { menuList ->
            adapter.submitList(menuList)
        }

        binding.seeallNote.setOnClickListener{
            startActivity(Intent(this, NotesDetailActivity::class.java))
        }

        binding.btnAdd.setOnClickListener {
            val currentQuantity = binding.quantity.text.toString().toInt()
            binding.quantity.text = getString(R.string.quantity_text, currentQuantity + 1)
        }

        binding.btnMinus.setOnClickListener {
            val currentQuantity = binding.quantity.text.toString().toInt()
            if (currentQuantity > 0) {
                binding.quantity.text = getString(R.string.quantity_text, currentQuantity - 1)
            }
        }

        binding.btnCart.setOnClickListener {
            val quantity = binding.quantity.text.toString().toInt()
            val cartItem = CartModel(
                id = intent.getStringExtra(MENU_ID).orEmpty(),
                name = intent.getStringExtra(MENU_NAME).orEmpty(),
                price = intent.getStringExtra(MENU_PRICE).orEmpty(),
                quantity = quantity.toString(),
                imageurl = intent.getStringExtra(MENU_IMAGE).orEmpty(),
            )

            val itemSummary = OrderSummaryModel(
                id = intent.getStringExtra(MENU_ID).orEmpty(),
                name = intent.getStringExtra(MENU_NAME).orEmpty(),
                preparationTime = binding.tvPickupTime.text.toString()
            )

            val cartPreferences = CartPreferences(this)
            val currentCartItems = cartPreferences.getCartItems().toMutableList()
            currentCartItems.add(cartItem)

            val currentItemSummaries = cartPreferences.getItemSummaries().toMutableList()
            currentItemSummaries.add(itemSummary)

            cartPreferences.saveCartItems(currentCartItems)
            cartPreferences.saveItemSummaries(currentItemSummaries)

            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val MENU_ID = "menu_id"
        const val MENU_NAME = "menu_name"
        const val MENU_DESCRIPTION = "menu_description"
        const val MENU_INGREDIENTS = "menu_ingredients"
        const val MENU_PREPARATIONTIME = "menu_preparationtime"
        const val MENU_PRICE = "menu_price"
        const val MENU_IMAGE = "menu_image"
    }
}