package com.mnrf.ejajan.view.main.merchant.ui.activeorder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.model.MerchantOrderModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository

class OrderConfirmationViewModel(private val repository: UserRepository) : ViewModel() {
    private val _orderList = MutableLiveData<List<MerchantOrderModel>>()
    val orderList: LiveData<List<MerchantOrderModel>> get() = _orderList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _orderDetail = MutableLiveData<MerchantOrderModel>()
    val orderDetail: LiveData<MerchantOrderModel> get() = _orderDetail

    val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData()
    }

    fun confirmPickup(orderId: String) {
        db.collection("order").document(orderId).update(
            "order_status", "Completed"
        ).addOnSuccessListener {
            Log.d(TAG, "Order status updated to Completed for ID: $orderId")
            _orderList.value = _orderList.value?.map { order ->
                if (order.id == orderId) {
                    order.copy(orderStatus = "Completed")
                } else {
                    order
                }
            }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error updating order status to Completed", exception)
        }
    }

    fun getStudentUidbyFaceContour(
        faceContour: String,
        callback: (String?) -> Unit
    ) {
        db.collection("studentprofiles")
            .whereEqualTo("facecontour", faceContour)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.e(TAG, "No student UID found for faceContour: $faceContour")
                    callback(null)
                } else {
                    val studentUid = result.documents.firstOrNull()?.getString("uid")
                    Log.d(TAG, "Fetched student UID: $studentUid")
                    callback(studentUid)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching student UID", exception)
                callback(null)
            }
    }

    companion object {
        const val TAG = "OrderConfirmationViewModel"
    }

}