package com.mnrf.ejajan.view.main.parent.ui.setting.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.pref.UserPreference
import com.mnrf.ejajan.data.pref.dataStore
import com.mnrf.ejajan.databinding.ActivityParentAddBinding
import com.mnrf.ejajan.databinding.ActivityParentProfileBinding

class ProfileParentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentProfileBinding
    private var currentImageUri: Uri? = null
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Profile"
        }

        userPreference = UserPreference.getInstance(dataStore)
        observeUserSession()

        binding.cvSettingImage.setOnClickListener {
            startGallery()
        }

        binding.btnSave.setOnClickListener {
            val intent = Intent(this, ProfileParentActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
            binding.imgAbout.setImageURI(it)
        }
    }

    private fun observeUserSession() {
        lifecycleScope.launchWhenStarted {
            userPreference.getSession().collect { user ->
                binding.edEmailProfile.apply {
                    setText(user.email)
                    isEnabled = false
                    isFocusable = false
                    isFocusableInTouchMode = false
                }
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}