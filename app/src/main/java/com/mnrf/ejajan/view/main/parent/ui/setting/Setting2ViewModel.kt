package com.mnrf.ejajan.view.main.parent.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mnrf.ejajan.data.pref.ThemePreferences
import com.mnrf.ejajan.data.repository.UserRepository
import kotlinx.coroutines.launch

class Setting2ViewModel(private val repository: UserRepository, private val pref: ThemePreferences) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}