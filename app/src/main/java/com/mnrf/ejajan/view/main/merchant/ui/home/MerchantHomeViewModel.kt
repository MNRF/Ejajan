package com.mnrf.ejajan.view.main.merchant.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.model.MenuModel
import com.mnrf.ejajan.data.model.MerchantOrderModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository

class MerchantHomeViewModel(private val repository: UserRepository) : ViewModel() {
    private val _orderList = MutableLiveData<List<MerchantOrderModel>>()
    val orderList: LiveData<List<MerchantOrderModel>> get() = _orderList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData()
    }

    private fun fetchMenuList() {
        getSession().observeForever { user ->
            if (user != null) {
                db.collection("order")
                    .whereEqualTo("merchant_uid", user.token)
                    .get()
                    .addOnSuccessListener { result ->
                        val orders = result.map { document ->
                            MerchantOrderModel(
                                id = document.id,
                                merchantUid = document.getString("merchant_uid") ?: "",
                                studentUid = document.getString("student_uid") ?: "",
                                menuId = document.getString("menu_uid") ?: "", //TODO: change to "menu_id"
                                menuName = document.getString("menu_name") ?: "",
                                menuImage = document.getString("menu_imageurl") ?: "",
                                menuQty = document.getString("menu_qty") ?: "",
                                menuPrice = document.getString("menu_price") ?: "",
                                orderPickupTime = document.getString("order_pickuptime") ?: "", //TODO add order pickup time in firestore
                                orderStatus = document.getString("order_status") ?: ""
                            )
                        }
                        _orderList.value = orders
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
        fetchMenuList()
    }

    companion object {
        const val TAG = "MerchantHomeViewModel"
    }
}