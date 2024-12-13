package com.mnrf.ejajan.view.main.parent.ui.topup

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.model.ParentProfileModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mnrf.ejajan.data.response.ApiTransactionRequest
import com.mnrf.ejajan.data.response.CustomerDetails
import com.mnrf.ejajan.data.response.TransactionResponse
import com.mnrf.ejajan.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopUpViewModel(private val repository: UserRepository) : ViewModel() {

    private val _parentProfile = MutableLiveData<ParentProfileModel?>()
    val parentProfile: MutableLiveData<ParentProfileModel?> get() = _parentProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _transactionResponse = MutableLiveData<TransactionResponse>()
    val transactionResponse: LiveData<TransactionResponse> get() = _transactionResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val db = Firebase.firestore

    private val _transactionStatus = MutableLiveData<String?>()
    val transactionStatus: LiveData<String?> get() = _transactionStatus

    /*fun fetchTransactionStatus(transactionId: String) {
        ApiConfig.getApiService().getTransactionStatus(transactionId).enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                if (response.isSuccessful) {
                    _transactionStatus.value = response.body()?.status
                } else {
                    _transactionStatus.value = "Error: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                _transactionStatus.value = "Failed to fetch status: ${t.message}"
                Log.e("TransactionStatus", t.message ?: "Unknown error")
            }
        })
    }*/

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData()
    }

    fun startTransaction(amount: Double) {
        _isLoading.value = true

        getSession().observeForever { user ->
            user?.let {
                fetchParentProfile(user.token) { profile ->
                    if (profile != null) {
                        val orderId = "order-${System.currentTimeMillis()}"
                        val customerDetails = CustomerDetails(
                            first_name = profile.name,
                            last_name = "",
                            email = profile.email,
                            phone = ""
                        )
                        val request = ApiTransactionRequest(
                            order_id = orderId,
                            gross_amount = amount,
                            customer_details = customerDetails
                        )

                        ApiConfig.getCreateTransactionService().createTransaction(request)
                            .enqueue(object : Callback<TransactionResponse> {
                                override fun onResponse(
                                    call: Call<TransactionResponse>,
                                    response: Response<TransactionResponse>
                                ) {
                                    _isLoading.value = false
                                    if (response.isSuccessful) {
                                        _transactionResponse.value = response.body()
                                    } else {
                                        _errorMessage.value =
                                            "Error: ${response.errorBody()?.string()}"
                                    }
                                }

                                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                                    _isLoading.value = false
                                    _errorMessage.value = t.message
                                }
                            })
                    } else {
                        _errorMessage.value = "Parent profile not found."
                        _isLoading.value = false
                    }
                }
            }
        }
    }


    fun topUp(amount: Int) {
        _isLoading.value = true

        // Fetch the parent profile first
        getSession().observeForever { user ->
            user?.let {
                fetchParentProfile(user.token) { profile ->
                    if (profile != null) {
                        val newBalance = (profile.balance.toIntOrNull() ?: 0) + amount
                        updateBalance(profile.id, newBalance)
                    } else {
                        Log.e(TAG, "Profile not found for user.")
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    fun checkTransactionStatus(orderId: String, callback: (String?) -> Unit) {
        println("QWERTY1 $orderId")
        ApiConfig.getCheckTransactionStatusService().getTransactionStatus(orderId)
            .enqueue(object : Callback<TransactionResponse> {
                override fun onResponse(
                    call: Call<TransactionResponse>,
                    response: Response<TransactionResponse>
                ) {
                    if (response.isSuccessful) {
                        val transactionStatus = response.body()?.transaction_status
                        Log.d(TAG, "Transaction status: $transactionStatus")
                        callback(transactionStatus)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e(TAG, "Error fetching status: $errorBody")
                        println("ERROR BODY: $errorBody")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                    Log.e(TAG, "Failed to fetch status: ${t.message}")
                    callback(null)
                }
            })
    }





    private fun fetchParentProfile(userToken: String, callback: (ParentProfileModel?) -> Unit) {
        db.collection("parentprofiles")
            .whereEqualTo("uid", userToken)
            .get()
            .addOnSuccessListener { result ->
                val profiles = result.documents.mapNotNull { doc ->
                    doc.toObject(ParentProfileModel::class.java)?.copy(id = doc.id)
                }
                val profile = profiles.firstOrNull()
                _parentProfile.value = profile
                callback(profile)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching parent profile", exception)
                _isLoading.value = false
                callback(null)
            }
    }

    private fun updateBalance(profileId: String, newBalance: Int) {
        viewModelScope.launch {
            try {
                db.collection("parentprofiles").document(profileId)
                    .update("balance", newBalance.toString()).await()
                Log.d(TAG, "Balance updated successfully.")
            } catch (exception: Exception) {
                Log.e(TAG, "Error updating balance", exception)
            } finally {
                _isLoading.value = false
            }
        }
    }

    companion object {
        const val TAG = "TopUpViewModel"
    }
}
