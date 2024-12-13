package com.mnrf.ejajan.view.main.merchant.ui.menu.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class MerchantAddMenuViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val db = Firebase.firestore
    val storage = Firebase.storage

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData().also {
            _isLoading.value = false
        }
    }

    suspend fun addMenu(
        merchantUid: String,
        menuName: String,
        menuDescription: String,
        menuIngredients: String,
        menuPreparationtime: String,
        menuPrice: String,
        menuImageFile: File
    ): Boolean {
        val uploadedImageUrl = uploadImage(menuImageFile)

        // Periksa apakah URL berhasil dibuat
        if (uploadedImageUrl.isEmpty()) {
            Log.e(TAG, "Image upload failed, aborting Firestore save")
            return false
        }

        val menuData = hashMapOf(
            "merchant_uid" to merchantUid,
            "menu_name" to menuName,
            "menu_description" to menuDescription,
            "menu_ingredients" to menuIngredients,
            "menu_preparationtime" to menuPreparationtime,
            "menu_price" to menuPrice,
            "menu_imageurl" to uploadedImageUrl
        )

        return try {
            db.collection("menus").add(menuData).await() // Tunggu hingga selesai
            Log.d(TAG, "Menu created successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error creating menu: ${e.localizedMessage}", e)
            false
        }
    }


    private suspend fun uploadImage(image: File): String {
        var downloadUrl = ""
        try {
            val storageRef: StorageReference = storage.reference
            val fileRef = storageRef.child("images/${image.name}")
            val stream = FileInputStream(image)

            // Upload gambar
            val uploadTask = fileRef.putStream(stream)
            uploadTask.await() // Tunggu hingga upload selesai

            // Dapatkan URL unduhan
            downloadUrl = fileRef.downloadUrl.await().toString()
            Log.d("UploadImage", "Image uploaded successfully: $downloadUrl")
        } catch (e: Exception) {
            Log.e("UploadImage", "Error uploading image: ${e.localizedMessage}", e)
        }
        return downloadUrl
    }


    fun addPrePackagedMenu (merchantUid: String, menuName: String, menuDescription: String, menuBrand: String,
                 menuPreparationtime: String) {
        val user = hashMapOf(
            "merchant_uid" to merchantUid,
            "menu_brand" to menuBrand,
            "menu_name" to menuName,
            "menu_description" to menuDescription,
            "menu_preparationtime" to menuPreparationtime,
        )

        db.collection("menus")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    companion object {
        const val TAG = "addMenu"
    }
}