package com.mnrf.ejajan.view.main.merchant.ui.menu.add

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
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
import com.mnrf.ejajan.view.main.merchant.ui.menu.MenuMerchantViewModel
import com.mnrf.ejajan.view.utils.ViewModelFactory
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMerchantAddMenuBinding

    private val merchantAddMenuViewModel: MerchantAddMenuViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val menuMerchantViewModel: MenuMerchantViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }


    private var isMenuSubmitted = false
    private var currentImageUri: Uri? = null
    private var uploadedImageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMerchantAddMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Add Menu"
        }

        binding.btnTambah.setOnClickListener {
            if (isMenuSubmitted) return@setOnClickListener

            if (currentImageUri == null) {
                Log.e("AddMenu", "No image selected")
                return@setOnClickListener
            }

            val file = uriToFile(currentImageUri!!)
            file.reduceFileImage()

            merchantAddMenuViewModel.getSession().observe(this) { user ->
                val merchantUid = user.token

                if (!isMenuSubmitted && merchantUid.isNotEmpty()) {
                    isMenuSubmitted = true
                    lifecycleScope.launch {
                        val isSuccess = merchantAddMenuViewModel.addMenu(
                            merchantUid,
                            binding.etFoodName.text.toString(),
                            binding.etDescription.text.toString(),
                            binding.etBasicIngredients.text.toString(),
                            binding.etAddPreparetime.text.toString(),
                            binding.etPriceFood.text.toString(),
                            file
                        )
                        if (isSuccess) {
                            startActivity(Intent(this@AddMenuActivity, MerchantActivity::class.java))
                        } else {
                            Log.e("AddMenu", "Failed to add menu")
                        }
                    }
                }
            }
        }

        binding.cvSettingImage.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    fun File.reduceFileImage(): File {
        val bitmap = BitmapFactory.decodeFile(this.path)
        var compressQuality = 80
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 500000 && compressQuality > 10)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(this))
        return this
    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver = contentResolver
        val tempFile = File.createTempFile("temp_", ".jpg", cacheDir)
        contentResolver.openInputStream(uri)?.use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        Log.d("UriToFile", "File created at ${tempFile.path}")
        return tempFile
    }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivAddMenu.setImageURI(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data // Handle the selected image URI or camera data
            if (imageUri != null) {
                // Handle the image from the gallery
                binding.ivAddMenu.setImageURI(imageUri)
            } else {
                // Handle the photo taken by the camera
                val bitmap = data?.extras?.get("data") as? Bitmap
                bitmap?.let {
                    binding.ivAddMenu.setImageBitmap(it)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}