package com.mnrf.ejajan.view.main.student.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.model.NotesModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository

class NotesDetailViewModel (private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _menuList = MutableLiveData<List<NotesModel>>()
    val menuList: LiveData<List<NotesModel>> get() = _menuList

    private val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData().also {
            _isLoading.value = false
        }
    }

    private fun fetchMenuList() {
        getSession().observeForever { user ->
            if (user != null) {
                db.collection("notes")
//                    .whereEqualTo("ford", "d")
                    .get()
                    .addOnSuccessListener { result ->
                        val notes = result.map { document ->
                            NotesModel(
                                id = document.id,
                                name = document.getString("name") ?: "",
                            )
                        }
                        _menuList.value = notes
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
        const val TAG = "NotesDetailViewModel"
    }
}