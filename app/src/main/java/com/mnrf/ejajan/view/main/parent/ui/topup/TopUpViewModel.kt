package com.mnrf.ejajan.view.main.parent.ui.topup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.model.ParentProfileModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TopUpViewModel(private val repository: UserRepository) : ViewModel() {

    private val _parentProfile = MutableLiveData<ParentProfileModel>()
    val parentProfile: LiveData<ParentProfileModel> get() = _parentProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData()
    }

    fun topUp(amount: Int) {
        _isLoading.value = true

        // Fetch the parent profile first
        getSession().observeForever { user ->
            user?.let {
                fetchParentProfile(user.token) { profile ->
                    if (profile != null) {
                        val newBalance = (profile.balance?.toIntOrNull() ?: 0) + amount
                        updateBalance(profile.id, newBalance)
                    } else {
                        Log.e(TAG, "Profile not found for user.")
                        _isLoading.value = false
                    }
                }
            }
        }
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
