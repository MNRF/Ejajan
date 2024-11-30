package com.mnrf.ejajan.data.repository

import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.pref.UserPreference
/*import com.mnrf.ejajan.data.retrofit.ApiService*/
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    /*val apiService: ApiService,*/
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            /*apiService: ApiService,*/
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(/*apiService, */userPreference)
            }.also { instance = it }
    }
}

