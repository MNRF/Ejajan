package com.mnrf.ejajan.view.main.parent.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnrf.ejajan.data.UserRepository
import kotlinx.coroutines.launch

class Setting2ViewModel(private val repository: UserRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}