package com.mnrf.ejajan.view.main.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnrf.ejajan.data.repository.UserRepository
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: UserRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}