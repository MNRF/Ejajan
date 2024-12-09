package com.mnrf.ejajan.view.main.parent.ui.student.delete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mnrf.ejajan.data.repository.ConstraintRepository

class DeleteConstraintViewModel(private val repository: ConstraintRepository) : ViewModel() {

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> get() = _deleteSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _allergiesMap = MutableLiveData<Map<String, String>>() // ID -> Name
    val allergiesMap: LiveData<Map<String, String>> get() = _allergiesMap

    private val _spendingMap = MutableLiveData<Map<String, Pair<String, String>>>() // ID -> (Amount, Period)
    val spendingMap: LiveData<Map<String, Pair<String, String>>> get() = _spendingMap

    private val _nutritionMap = MutableLiveData<Map<String, Pair<String, String>>>() // ID -> (Amount, Period)
    val nutritionMap: LiveData<Map<String, Pair<String, String>>> get() = _nutritionMap

    fun fetchAllergies(studetId: String) {
        repository.getAllergiesWithIds(studentUid = studetId ,
            onSuccess = { data -> _allergiesMap.postValue(data) },
            onFailure = { e -> _errorMessage.postValue("Error fetching allergies: ${e.message}") }
        )
    }

    fun fetchSpending(studetId: String) {
        repository.getSpendingWithIds(studentUid = studetId,
            onSuccess = { data -> _spendingMap.postValue(data) },
            onFailure = { e -> _errorMessage.postValue("Error fetching spendings: ${e.message}") }
        )
    }

    fun fetchNutrition(studetId: String) {
        repository.getNutritionWithIds(studentUid = studetId,
            onSuccess = { data -> _nutritionMap.postValue(data) },
            onFailure = { e -> _errorMessage.postValue("Error fetching spending data: ${e.message}") }
        )
    }

    fun deleteAllergy(allergyId: String) {
        repository.deleteAllergyById(
            allergyId,
            onSuccess = { _deleteSuccess.postValue(true) },
            onFailure = { e -> _errorMessage.postValue("Error deleting allergy: ${e.message}") }
        )
    }

    fun deleteSpending(spendingId: String) {
        repository.deleteSpendingById(
            spendingId,
            onSuccess = { _deleteSuccess.postValue(true) },
            onFailure = { e -> _errorMessage.postValue("Error deleting spending: ${e.message}") }
        )
    }

    fun deleteNutrition(nutritionId: String) {
        repository.deleteNutritionById(
            nutritionId,
            onSuccess = { _deleteSuccess.postValue(true) },
            onFailure = { e -> _errorMessage.postValue("Error deleting spending: ${e.message}") }
        )
    }

}

