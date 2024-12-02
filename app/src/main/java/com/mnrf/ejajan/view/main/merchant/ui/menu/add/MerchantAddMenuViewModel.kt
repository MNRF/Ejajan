package com.mnrf.ejajan.view.main.merchant.ui.menu.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository

class MerchantAddMenuViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData().also {
            _isLoading.value = false
        }
    }

    fun addMenu (merchantUid: String, menuName: String, menuDescription: String, menuIngredients: String,
                 menuPreparationtime: String, menuPrice: String): Boolean {
        var isSuccess: Boolean
        try {
            val user = hashMapOf(
                "merchant_uid" to merchantUid,
                "menu_name" to menuName,
                "menu_description" to menuDescription,
                "menu_ingredients" to menuIngredients,
                "menu_preparationtime" to menuPreparationtime,
                "menu_price" to menuPrice
            )
            db.collection("menus")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            isSuccess = true
        }catch (e: Exception) {
            e.printStackTrace()
            isSuccess = false
        }
        return isSuccess
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