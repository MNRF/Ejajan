package com.mnrf.ejajan.view.main.student

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.model.MenuModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository
import com.mnrf.ejajan.view.main.student.healty.HealtyViewModel
import com.mnrf.ejajan.view.main.student.healty.HealtyViewModel.Companion
import com.mnrf.ejajan.view.main.student.special.SpecialOffersViewModel
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: UserRepository) : ViewModel() {

    private val _foodMenuList = MutableLiveData<List<MenuModel>>()
    val foodMenuList: LiveData<List<MenuModel>> get() = _foodMenuList

    private val _drinkMenuList = MutableLiveData<List<MenuModel>>()
    val drinkMenuList: LiveData<List<MenuModel>> get() = _drinkMenuList

    private val _healtMenuList = MutableLiveData<List<MenuModel>>()
    val healtMenuList: LiveData<List<MenuModel>> get() = _healtMenuList

    private val _specialMenuList = MutableLiveData<List<MenuModel>>()
    val specialMenuList: LiveData<List<MenuModel>> get() = _specialMenuList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    private fun fetchMenuListFood() {
        getSession().observeForever { user ->
            if (user != null) {
                db.collection("menus")
                    .whereEqualTo("ford", "f")
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
                        }.take(4)
                        _foodMenuList.value = menus
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error fetching menu list", exception)
                        _isLoading.value = false
                    }
            }
        }
    }

    private fun fetchMenuListDrink() {
        getSession().observeForever { user ->
            if (user != null) {
                db.collection("menus")
                    .whereEqualTo("ford", "d")
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
                        }.take(4)
                        _drinkMenuList.value = menus
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error fetching menu list", exception)
                        _isLoading.value = false
                    }
            }
        }
    }

    private fun fetchMenuListHealthy() {
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
                        }.take(4)
                        _healtMenuList.value = menus
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(HealtyViewModel.TAG, "Error fetching menu list", exception)
                        _isLoading.value = false
                    }
            }
        }
    }

    private fun fetchMenuListSpecial() {
        getSession().observeForever { user ->
            if (user != null) {
                db.collection("menus")
                    .whereEqualTo("isDiscount", "10")
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
                        }.take(4)
                        _specialMenuList.value = menus
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(SpecialOffersViewModel.TAG, "Error fetching menu list", exception)
                        _isLoading.value = false
                    }
            }
        }
    }

    init {
        fetchMenuListFood()
        fetchMenuListDrink()
        fetchMenuListHealthy()
        fetchMenuListSpecial()
    }

    companion object {
        const val TAG = "StudentViewModel"
    }
}