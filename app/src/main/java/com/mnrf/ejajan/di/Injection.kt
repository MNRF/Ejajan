package com.mnrf.ejajan.di

import android.content.Context
import com.mnrf.ejajan.data.UserRepository
import com.mnrf.ejajan.data.pref.UserModel
import com.mnrf.ejajan.data.pref.UserPreference
import com.mnrf.ejajan.data.pref.dataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().firstOrNull() ?: UserModel("", "", false) }
        val token = user.token

/*        val apiService = if (token.isNotEmpty()) {
            ApiConfig.getApiService(token)
        } else {
            ApiConfig.getApiService()
        }*/

        return UserRepository.getInstance(/*apiService, */pref)
    }
}