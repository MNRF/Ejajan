package com.mnrf.ejajan.view.main.student.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository
import java.time.Instant

class CartViewModel (private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var transactionId = ""

    private val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData().also {
            _isLoading.value = false
        }
    }

    fun fetchUidsAndCreateTransaction(
        totalTransaction: String,
        onSuccess: () -> Unit
    ) {
        db.collection("studentprofiles")
            .get()
            .addOnSuccessListener { studentSnapshot ->
                val studentUid = studentSnapshot.documents.firstOrNull()?.getString("uid") ?: ""
                db.collection("merchantprofiles")
                    .get()
                    .addOnSuccessListener { merchantSnapshot ->
                        val merchantUid = merchantSnapshot.documents.firstOrNull()?.getString("uid") ?: ""
                        db.collection("parentprofiles")
                            .get()
                            .addOnSuccessListener { parentSnapshot ->
                                val parentUid = parentSnapshot.documents.firstOrNull()?.getString("uid") ?: ""
                                transaction(merchantUid, studentUid, parentUid, totalTransaction)
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Failed to fetch parent UID", e)
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Failed to fetch merchant UID", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Failed to fetch student UID", e)
            }
    }

    private fun transaction
                (merchantUid: String,
                 studentUid: String,
                 parentUid: String,
                 totalTransaction: String
    ): Boolean {
        var isSuccess: Boolean

        try {
            val transaction = hashMapOf(
                "merchant_uid" to merchantUid,
                "student_uid" to studentUid,
                "parent_uid" to parentUid,
                "total_transaction" to totalTransaction,
                "date_created" to Timestamp.now()
            )
            db.collection("transaction")
                .add(transaction)
                .addOnSuccessListener { documentReference ->
                    transactionId = documentReference.id
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

    companion object {
        const val TAG = "transaction"
    }
}