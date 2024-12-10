package com.mnrf.ejajan.view.main.parent.ui.setting.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.mnrf.ejajan.data.model.ParentProfileModel
import com.mnrf.ejajan.data.model.ProfileModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class ProfileParentViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _parentProfileData = MutableLiveData<ProfileModel?>()
    val parentProfileData: LiveData<ProfileModel?> = _parentProfileData

    private val db = Firebase.firestore
    private val storage = Firebase.storage

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData().also {
            _isLoading.value = false
        }
    }

    fun getParentProfile(uid: String): LiveData<ProfileModel?> {
        val liveData = MutableLiveData<ProfileModel?>()

        _isLoading.value = true
        db.collection("parentprofiles").document(uid).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(ProfileModel::class.java)
                    liveData.value = user
                } else {
                    liveData.value = null
                }
                _isLoading.value = false
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching parent profile", exception)
                _isLoading.value = false
            }

        return liveData
    }

    suspend fun updateProfile(
        uid: String,
        newName: String,
        newEmail: String,
        newImageUrl: String
    ) {
        try {
            db.collection("parentprofiles").document(uid).update(
                "name", newName,
                "email", newEmail,
                "imageurl", newImageUrl
            ).await()
        } catch (exception: Exception) {
            Log.e(TAG, "Error updating profiles", exception)
        }
    }

    suspend fun uploadImage(image: File): String {
        return try {
            val storageRef = storage.reference
            val fileRef = storageRef.child("profile/${image.name}")
            val stream = withContext(Dispatchers.IO) {
                FileInputStream(image)
            }
            fileRef.putStream(stream).await()
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading image", e)
            ""
        }
    }

    suspend fun deleteImage(imageUrl: String) {
        try {
            val storageRef = storage.getReferenceFromUrl(imageUrl)
            storageRef.delete().await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting image", e)
        }
    }


    companion object {
        const val TAG = "ProfileParentViewModel"
    }
}