package com.mnrf.ejajan.view.main.parent.ui.student.change

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.ConstraintRepository
import kotlinx.coroutines.flow.Flow

class ChangeConstraintViewModel(
    private val repository: ConstraintRepository
) : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _updateAllergyStatus = MutableLiveData<String>()
    val updateAllergyStatus: LiveData<String> get() = _updateAllergyStatus

    private val _updateSpendingStatus = MutableLiveData<String>()
    val updateSpendingStatus: LiveData<String> get() = _updateSpendingStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _allergiesMap = MutableLiveData<Map<String, String>>() // ID -> Name
    val allergiesMap: LiveData<Map<String, String>> get() = _allergiesMap

    private val _spendingMap = MutableLiveData<Map<String, Pair<String, String>>>() // ID -> (Amount, Period)
    val spendingMap: LiveData<Map<String, Pair<String, String>>> get() = _spendingMap

    // Fetch session details
    fun getSession(): Flow<UserModel> {
        return repository.getSession()
    }

    // Fetch allergies from repository
    fun fetchAllergies() {
        repository.getAllergiesWithIds(
            onSuccess = { data -> _allergiesMap.postValue(data) },
            onFailure = { e -> _errorMessage.postValue("Error fetching allergies: ${e.message}") }
        )
    }

    // Fetch spending constraints from repository
    fun fetchSpending() {
        repository.getSpendingWithIds(
            onSuccess = { data -> _spendingMap.postValue(data) },
            onFailure = { e -> _errorMessage.postValue("Error fetching spending data: ${e.message}") }
        )
    }

    // Update allergy constraint
    fun updateAllergy(allergyId: String, newName: String) {
        repository.updateAllergy(allergyId, newName)
            .addOnSuccessListener {
                _updateAllergyStatus.postValue("Allergy updated successfully")
            }
            .addOnFailureListener { exception ->
                _updateAllergyStatus.postValue("Error updating allergy: ${exception.message}")
            }
    }

    // Update spending constraint
    fun updateSpending(spendingId: String, newAmount: String, newPeriod: String) {
        repository.updateSpending(spendingId, newAmount, newPeriod)
            .addOnSuccessListener {
                _updateSpendingStatus.postValue("Spending updated successfully")
            }
            .addOnFailureListener { exception ->
                _updateSpendingStatus.postValue("Error updating spending: ${exception.message}")
            }
    }
}
