package com.mnrf.ejajan.view.main.merchant.ui.menu.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository

class DetailMenuViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData().also {
            _isLoading.value = false
        }
    }

    fun updateMenu (menuId: String, newName: String, newDescription: String, newIngredients: String,
                    newPreparationtime: String, newPrice: String) {
        db.collection("menus").document(menuId).update(
            "menu_name", newName,
            "menu_description", newDescription,
            "menu_ingredients", newIngredients,
            "menu_preparationtime", newPreparationtime,
            "menu_price", newPrice
        ).addOnSuccessListener {  }.addOnFailureListener{ exception ->
            Log.e(TAG, "Error updating menu", exception)
            _isLoading.value = false
        }
    }

    fun deleteMenu (menuId: String) {
        db.collection("menus").document(menuId).delete()
            .addOnSuccessListener {  }.addOnFailureListener{ exception ->
            Log.e(TAG, "Error updating menu", exception)
            _isLoading.value = false
        }
    }

    companion object {
        const val TAG = "DetailMenuViewModel"
    }
}