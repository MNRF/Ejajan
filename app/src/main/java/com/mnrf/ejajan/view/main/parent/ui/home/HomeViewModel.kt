package com.mnrf.ejajan.view.main.parent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.model.MenuModel
import com.mnrf.ejajan.data.model.MerchantProfileModel
import com.mnrf.ejajan.data.model.ParentProfileModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository
import com.mnrf.ejajan.view.main.merchant.ui.setting.SettingViewModel
import com.mnrf.ejajan.view.main.merchant.ui.setting.SettingViewModel.Companion

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private val _parentProfile = MutableLiveData<ParentProfileModel>()
    val parentProfile: LiveData<ParentProfileModel> get() = _parentProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val text: LiveData<String> = _text

    val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData()
    }

    private fun fetchParentProfile() {
        _isLoading.value = true
        getSession().observeForever { user ->
            user?.let {
                db.collection("parentprofiles")
                    .whereEqualTo("uid", user.token)
                    .get()
                    .addOnSuccessListener { result ->
                        val profiles = result.documents.mapNotNull { doc ->
                            doc.toObject(ParentProfileModel::class.java)
                        }
                        _parentProfile.value = profiles.firstOrNull()
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(SettingViewModel.TAG, "Error fetching parent profile", exception)
                        _isLoading.value = false
                    }
            }
        }
    }

    init {
        fetchParentProfile()
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}