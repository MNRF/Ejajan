package com.mnrf.ejajan.view.main.parent.ui.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.model.CartModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository

class ReportViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _orderList = MutableLiveData<List<CartModel>>()
    val orderList: LiveData<List<CartModel>> get() = _orderList

    val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData()
    }

    private fun fetchOrderList() {
        getSession().observeForever { user ->
            if (user != null) {
                db.collection("order")
                    .get()
                    .addOnSuccessListener { result ->
                        val order = result.map { document ->
                            CartModel(
                                id = document.id,
                                name = document.getString("menu_name") ?: "",
                                price = document.getString("menu_price") ?: "",
                                quantity = document.getString("menu_quantity") ?: "",
                                imageurl = document.getString("menu_imageurl") ?: "",
                            )
                        }
                        _orderList.value = order
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
        fetchOrderList()
    }

    companion object {
        const val TAG = "ReportViewModel"
    }
}