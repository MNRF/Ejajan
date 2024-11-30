package com.mnrf.ejajan.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mnrf.ejajan.data.repository.UserRepository
import com.mnrf.ejajan.data.model.UserModel
import kotlinx.coroutines.launch

class LoginParentMerchantViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData().also {
            _isLoading.value = false
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    saveUserSession(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    // Handle failure case here if needed
                }
            }
    }

    private fun saveUserSession(user: FirebaseUser?) {
        user?.let {
            val email = it.email ?: ""
            val uid = it.uid
            val userModel = UserModel(email, uid, true)
            saveSession(userModel)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}