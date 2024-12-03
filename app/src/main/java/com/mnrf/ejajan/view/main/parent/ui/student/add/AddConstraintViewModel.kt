package com.mnrf.ejajan.view.main.parent.ui.student.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.data.model.SpendingModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.ConstraintRepository
import kotlinx.coroutines.launch

class AddConstraintViewModel(
    private val repository: ConstraintRepository,
) : ViewModel() {
    fun getSession(): Flow<UserModel> {
        return repository.getSession()
    }

    fun addAllergy(
        allergy: AllergyModel,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            repository.addAllergy(allergy, onSuccess, onFailure)
        }
    }

    fun addSpending(
        spending: SpendingModel,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            repository.addSpending(spending, onSuccess, onFailure)
        }
    }
}