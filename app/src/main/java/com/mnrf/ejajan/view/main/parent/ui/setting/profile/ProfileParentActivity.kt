package com.mnrf.ejajan.view.main.parent.ui.setting.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.mnrf.ejajan.data.pref.UserPreference
import com.mnrf.ejajan.data.pref.dataStore
import com.mnrf.ejajan.databinding.ActivityParentProfileBinding
import com.mnrf.ejajan.view.utils.ViewModelFactory
import kotlinx.coroutines.launch
import java.io.File

class ProfileParentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentProfileBinding
    private var currentImageUri: Uri? = null
    private lateinit var userPreference: UserPreference

    private val profileParentViewModel: ProfileParentViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

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

        profileParentViewModel.getParentProfile("tJTJGtfYyLfHerBBI0tpeqWdFg72")
        observeParentProfile()
        saveProfile()

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

    private fun observeParentProfile() {
        profileParentViewModel.parentProfileData.observe(this) { profile ->
            binding.edUsernameProfile.setText(profile?.name ?: "")
            binding.edEmailProfile.setText(profile?.email ?: "")
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

    private fun saveProfile() {
        val name = binding.edUsernameProfile.text.toString()
        val email = binding.edEmailProfile.text.toString()

        if (currentImageUri != null) {
            val file = File(currentImageUri!!.path ?: "")
            lifecycleScope.launch {
                try {
                    // Upload image first
                    val imageUrl = profileParentViewModel.uploadImage(file)
                    profileParentViewModel.updateProfile(
                        "tJTJGtfYyLfHerBBI0tpeqWdFg72",
                        name,
                        email,
                        imageUrl
                    )
                    Log.d("SaveProfile", "Profile updated successfully")
                } catch (e: Exception) {
                    Log.e("SaveProfile", "Failed to save profile", e)
                }
            }
        } else {
            lifecycleScope.launch {
                profileParentViewModel.updateProfile(
                    "tJTJGtfYyLfHerBBI0tpeqWdFg72",
                    name,
                    email,
                    "" // No new image uploaded
                )
                Log.d("SaveProfile", "Profile updated without new image")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}