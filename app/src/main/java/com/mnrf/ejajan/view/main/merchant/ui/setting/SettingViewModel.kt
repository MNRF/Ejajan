package com.mnrf.ejajan.view.main.merchant.ui.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.model.MenuModel
import com.mnrf.ejajan.data.model.MerchantProfileModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository
import kotlinx.coroutines.launch

class SettingViewModel (private val repository: UserRepository) : ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _merchantProfile = MutableLiveData<MerchantProfileModel?>()
    val merchantProfile: LiveData<MerchantProfileModel?> = _merchantProfile

    private val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    init {
        fetchMerchantProfile()
    }

    private fun fetchMerchantProfile() {
        _isLoading.value = true
        getSession().observeForever { user ->
            user?.let {
                db.collection("merchantprofiles")
                    .whereEqualTo("uid", user.token)
                    .get()
                    .addOnSuccessListener { result ->
                        val profiles = result.documents.mapNotNull { doc ->
                            doc.toObject(MerchantProfileModel::class.java)
                        }
                        _merchantProfile.value = profiles.firstOrNull()
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error fetching merchant profile", exception)
                        _isLoading.value = false
                    }
            }
        }
    }

    fun toggleMerchantOpen(isOpen: Boolean) {
        val updatedProfile = _merchantProfile.value ?: return
        val updatedDaysOpen = (if (isOpen) "1" else "0") + updatedProfile.daysopen.drop(1)
        db.collection("merchantprofiles").whereEqualTo("uid", updatedProfile.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentId = querySnapshot.documents[0].id
                    db.collection("merchantprofiles").document(documentId)
                        .update("daysopen", updatedDaysOpen)
                        .addOnSuccessListener {
                            _merchantProfile.value = updatedProfile.copy(daysopen = updatedDaysOpen)
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Error updating merchant open/close status", exception)
                        }
                } else {
                    Log.e(TAG, "No document found with uid: ${updatedProfile.uid}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error querying merchant profile", exception)
            }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    companion object {
        const val TAG = "SettingViewModel"
    }
}