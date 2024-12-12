// File: com/mnrf/ejajan/view/main/merchant/ui/menu/detail/DetailMenuActivity.kt
package com.mnrf.ejajan.view.main.merchant.ui.menu.detail

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.mnrf.ejajan.databinding.ActivityMerchantDetailMenuBinding
import com.mnrf.ejajan.view.main.merchant.MerchantActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream

class DetailMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMerchantDetailMenuBinding
    private val detailMenuViewModel: DetailMenuViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var selectedImageUri: Uri? = null
    private var currentImageUrl: String? = null

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            binding.imgAbout.setImageURI(uri)
        } else {
            Log.d("DetailMenuActivity", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMerchantDetailMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Detail Menu"
        }

        currentImageUrl = intent.getStringExtra(MENU_IMAGE)
        Glide.with(binding.imgAbout.context)
            .load(currentImageUrl)
            .into(binding.imgAbout)
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

            CoroutineScope(Dispatchers.IO).launch {
                val imageUrl = selectedImageUri?.let { uri ->
                    uriToFile(uri).reduceFileImage().let { file ->
                        detailMenuViewModel.uploadImage(file)
                    }
                }

                if (imageUrl != null && currentImageUrl != null) {
                    detailMenuViewModel.deleteImage(currentImageUrl!!)
                }

                detailMenuViewModel.updateMenu(
                    menuId, name, description, ingredients, preparationtime, price, imageUrl ?: currentImageUrl ?: ""
                )

                startActivity(Intent(this@DetailMenuActivity, MerchantActivity::class.java))
            }
        }

        binding.btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (currentImageUrl != null) {
                    detailMenuViewModel.deleteImage(currentImageUrl!!)
                }
                detailMenuViewModel.deleteMenu(menuId)
                startActivity(Intent(this@DetailMenuActivity, MerchantActivity::class.java))
            }
        }

        binding.cvSettingImage.setOnClickListener {
            launcherGallery.launch("image/*")
        }
    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver = contentResolver
        val tempFile = File.createTempFile("temp_", ".jpg", cacheDir)
        contentResolver.openInputStream(uri)?.use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }

    fun File.reduceFileImage(): File {
        val file = this
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}