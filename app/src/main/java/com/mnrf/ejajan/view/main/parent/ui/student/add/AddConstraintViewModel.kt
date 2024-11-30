package com.mnrf.ejajan.view.main.parent.ui.student.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.data.model.SpendingModel
import com.mnrf.ejajan.data.repository.ConstraintRepository

class AddConstraintViewModel(private val repository: ConstraintRepository) : ViewModel() {
    private val _allergyList = MutableLiveData<List<AllergyModel>>()
    val allergyList: LiveData<List<AllergyModel>> get() = _allergyList

    private val _spendingList = MutableLiveData<List<SpendingModel>>()
    val spendingList: LiveData<List<SpendingModel>> get() = _spendingList

    fun addAllergy(allergy: AllergyModel) {
        repository.addAllergy(allergy, {
            loadAllergies()
        }, { e ->
            Log.e("AddConstraintVM", "Failed to add allergy: ${e.message}")
        })
    }

     fun loadAllergies() {
        repository.getAllergies({ allergies ->
            _allergyList.value = allergies
        }, { e ->
            Log.e("AddConstraintVM", "Failed to load allergies: ${e.message}")
        })
    }

    fun addSpending(spending: SpendingModel) {
        repository.addSpending(spending, {
            loadSpending()
        }, { e ->
            Log.e("AddConstraintVM", "Failed to add spending: ${e.message}")
        })
    }

    fun loadSpending() {
        repository.getSpending({ spending ->
            _spendingList.value = spending
        }, { e ->
            Log.e("AddConstraintVM", "Failed to load spending: ${e.message}")
        })
    }
}