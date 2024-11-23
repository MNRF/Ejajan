package com.mnrf.ejajan.view.utils

import android.content.Context
import android.content.SharedPreferences

class OnboardingPreferences(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val ONBOARDING_COMPLETED = "onboarding_completed"
        private const val USER_ROLE = "user_role"
    }

    fun setOnboardingCompleted(completed: Boolean) {
        preferences.edit().putBoolean(ONBOARDING_COMPLETED, completed).apply()
    }

    fun isOnboardingCompleted(): Boolean {
        return preferences.getBoolean(ONBOARDING_COMPLETED, false)
    }

    fun setUserRole(role: String) {
        preferences.edit().putString(USER_ROLE, role).apply()
    }

    fun getUserRole(): String? {
        return preferences.getString(USER_ROLE, null)
    }
}