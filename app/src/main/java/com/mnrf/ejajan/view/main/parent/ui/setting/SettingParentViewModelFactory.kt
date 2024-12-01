package com.mnrf.ejajan.view.main.parent.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mnrf.ejajan.data.repository.ProfileRepository
import com.mnrf.ejajan.view.main.parent.ui.setting.profile.ProfileParentViewModel

class SettingParentViewModelFactory(private val repository: ProfileRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfileParentViewModel::class.java)) {
            ProfileParentViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
