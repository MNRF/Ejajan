package com.mnrf.ejajan.view.main.student.healty

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.model.MenuModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository

class HealtyViewModel (private val repository: UserRepository) : ViewModel() {
    private val _menuList = MutableLiveData<List<MenuModel>>()
    val menuList: LiveData<List<MenuModel>> get() = _menuList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData()
    }

    fun fetchMenuList(s: String) {
        getSession().observeForever { user ->
            if (user != null) {
                db.collection("menus")
                    .whereEqualTo("isHealthy", "1")
                    .get()
                    .addOnSuccessListener { result ->
                        val menus = result.map { document ->
                            MenuModel(
                                id = document.id,
                                name = document.getString("menu_name") ?: "",
                                description = document.getString("menu_description") ?: "",
                                ingredients = document.getString("menu_ingredients") ?: "",
                                preparationtime = document.getString("menu_preparationtime") ?: "",
                                price = document.getString("menu_price") ?: "",
                                imageurl = document.getString("menu_imageurl") ?: "",
                            )
                        }
                        _menuList.value = menus
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error fetching menu list", exception)
                        _isLoading.value = false
                    }
            }
        }
    }

    fun fetchMenuLowList(s: String) {
        getSession().observeForever { user ->
            if (user != null) {
                db.collection("menus")
                    .whereEqualTo("isLowCalories", "1")
                    .get()
                    .addOnSuccessListener { result ->
                        val menus = result.map { document ->
                            MenuModel(
                                id = document.id,
                                name = document.getString("menu_name") ?: "",
                                description = document.getString("menu_description") ?: "",
                                ingredients = document.getString("menu_ingredients") ?: "",
                                preparationtime = document.getString("menu_preparationtime") ?: "",
                                price = document.getString("menu_price") ?: "",
                                imageurl = document.getString("menu_imageurl") ?: "",
                            )
                        }
                        _menuList.value = menus
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error fetching menu list", exception)
                        _isLoading.value = false
                    }
            }
        }
    }

    fun fetchMenuHighList(s: String) {
        getSession().observeForever { user ->
            if (user != null) {
                db.collection("menus")
                    .whereEqualTo("isHighProtein", "1")
                    .get()
                    .addOnSuccessListener { result ->
                        val menus = result.map { document ->
                            MenuModel(
                                id = document.id,
                                name = document.getString("menu_name") ?: "",
                                description = document.getString("menu_description") ?: "",
                                ingredients = document.getString("menu_ingredients") ?: "",
                                preparationtime = document.getString("menu_preparationtime") ?: "",
                                price = document.getString("menu_price") ?: "",
                                imageurl = document.getString("menu_imageurl") ?: "",
                            )
                        }
                        _menuList.value = menus
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error fetching menu list", exception)
                        _isLoading.value = false
                    }
            }
        }
    }

    fun fetchMenuVegetariantList(s: String) {
        getSession().observeForever { user ->
            if (user != null) {
                db.collection("menus")
                    .whereEqualTo("isVegetarian", "1")
                    .get()
                    .addOnSuccessListener { result ->
                        val menus = result.map { document ->
                            MenuModel(
                                id = document.id,
                                name = document.getString("menu_name") ?: "",
                                description = document.getString("menu_description") ?: "",
                                ingredients = document.getString("menu_ingredients") ?: "",
                                preparationtime = document.getString("menu_preparationtime") ?: "",
                                price = document.getString("menu_price") ?: "",
                                imageurl = document.getString("menu_imageurl") ?: "",
                            )
                        }
                        _menuList.value = menus
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error fetching menu list", exception)
                        _isLoading.value = false
                    }
            }
        }
    }

    init {
        fetchMenuList("isHealthy")
        fetchMenuLowList("isLowCalories")
        fetchMenuHighList("isHighProtein")
        fetchMenuVegetariantList("isVegetarian")
    }

    companion object {
        const val TAG = "HealthyViewModel"
    }
}