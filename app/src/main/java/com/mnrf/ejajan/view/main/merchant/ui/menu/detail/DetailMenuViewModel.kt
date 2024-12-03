// File: com/mnrf/ejajan/view/main/merchant/ui/menu/detail/DetailMenuViewModel.kt
package com.mnrf.ejajan.view.main.merchant.ui.menu.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream

class DetailMenuViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val db = Firebase.firestore
    private val storage = Firebase.storage

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData().also {
            _isLoading.value = false
        }
    }

    suspend fun updateMenu(
        menuId: String,
        newName: String,
        newDescription: String,
        newIngredients: String,
        newPreparationtime: String,
        newPrice: String,
        newImageUrl: String
    ) {
        try {
            db.collection("menus").document(menuId).update(
                "menu_name", newName,
                "menu_description", newDescription,
                "menu_ingredients", newIngredients,
                "menu_preparationtime", newPreparationtime,
                "menu_price", newPrice,
                "menu_imageurl", newImageUrl
            ).await()
        } catch (exception: Exception) {
            Log.e(TAG, "Error updating menu", exception)
        }
    }

    suspend fun uploadImage(image: File): String {
        return try {
            val storageRef = storage.reference
            val fileRef = storageRef.child("images/${image.name}")
            val stream = FileInputStream(image)
            fileRef.putStream(stream).await()
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading image", e)
            ""
        }
    }

    suspend fun deleteImage(imageUrl: String) {
        try {
            val storageRef = storage.getReferenceFromUrl(imageUrl)
            storageRef.delete().await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting image", e)
        }
    }

    fun deleteMenu(menuId: String) {
        db.collection("menus").document(menuId).delete()
            .addOnSuccessListener { Log.d(TAG, "Menu deleted successfully") }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error deleting menu", exception)
            }
    }

    companion object {
        const val TAG = "DetailMenuViewModel"
    }
}