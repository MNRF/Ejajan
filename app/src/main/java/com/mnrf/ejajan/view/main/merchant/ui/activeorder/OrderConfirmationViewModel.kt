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
import okhttp3.internal.wait

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

    private fun chargeBalanceFromParent(order: MerchantOrderModel, onComplete: (Boolean) -> Unit) {
        val chargedAmount = order.menuPrice.toLong() * order.menuQty.toLong()
        db.collection("studentprofiles").whereEqualTo("uid", order.studentUid)
            .get()
            .addOnSuccessListener { studentDocs ->
                if (studentDocs.isEmpty) {
                    Log.e(TAG, "No parent UID found for student UID: ${order.studentUid}")
                    onComplete(false)
                    return@addOnSuccessListener
                }
                val parentUid = studentDocs.documents.first().getString("parent_uid").orEmpty()

                db.collection("parentprofiles").whereEqualTo("uid", parentUid)
                    .get()
                    .addOnSuccessListener { parentDocs ->
                        if (parentDocs.isEmpty) {
                            Log.e(TAG, "No parent profile found for UID: $parentUid")
                            onComplete(false)
                            return@addOnSuccessListener
                        }
                        val parentDoc = parentDocs.documents.first()
                        val parentBalance = parentDoc.getString("balance")?.toLongOrNull() ?: 0L
                        val updatedBalance = parentBalance - chargedAmount

                        if (updatedBalance < 0) {
                            Log.e(TAG, "Insufficient balance for parent UID: $parentUid")
                            onComplete(false)
                            return@addOnSuccessListener
                        }

                        db.collection("parentprofiles").document(parentDoc.id)
                            .update("balance", updatedBalance.toString())
                            .addOnSuccessListener {
                                Log.d(TAG, "Parent balance updated successfully.")
                                onComplete(true)
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error updating parent balance", e)
                                onComplete(false)
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error fetching parent profile", e)
                        onComplete(false)
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error fetching student profile", e)
                onComplete(false)
            }
    }

    private fun transferBalancetoMerchant(order: MerchantOrderModel, onComplete: (Boolean) -> Unit) {
        val chargedAmount = order.menuPrice.toLong() * order.menuQty.toLong()
        db.collection("merchantprofiles").whereEqualTo("uid", order.merchantUid)
            .get()
            .addOnSuccessListener { merchantDocs ->
                if (merchantDocs.isEmpty) {
                    Log.e(TAG, "No merchant profile found for UID: ${order.merchantUid}")
                    onComplete(false)
                    return@addOnSuccessListener
                }
                val merchantDoc = merchantDocs.documents.first()
                val merchantBalance = merchantDoc.getString("balance")?.toLongOrNull() ?: 0L
                val updatedBalance = merchantBalance + chargedAmount

                db.collection("merchantprofiles").document(merchantDoc.id)
                    .update("balance", updatedBalance.toString())
                    .addOnSuccessListener {
                        Log.d(TAG, "Merchant balance updated successfully.")
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error updating merchant balance", e)
                        onComplete(false)
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error fetching merchant profile", e)
                onComplete(false)
            }
    }

    fun confirmPickup(orderConfirmed: MerchantOrderModel) {
        chargeBalanceFromParent(orderConfirmed) { parentSuccess ->
            if (parentSuccess) {
                transferBalancetoMerchant(orderConfirmed) { merchantSuccess ->
                    if (merchantSuccess) {
                        db.collection("order").document(orderConfirmed.id)
                            .update("order_status", "Completed")
                            .addOnSuccessListener {
                                Log.d(TAG, "Order status updated to Completed for ID: ${orderConfirmed.id}")
                                _orderList.value = _orderList.value?.map { order ->
                                    if (order.id == orderConfirmed.id) {
                                        order.copy(orderStatus = "Completed")
                                    } else {
                                        order
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e(TAG, "Error updating order status to Completed", exception)
                            }
                    } else {
                        Log.e(TAG, "Failed to transfer balance to merchant.")
                    }
                }
            } else {
                Log.e(TAG, "Failed to charge balance from parent.")
            }
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