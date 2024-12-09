package com.mnrf.ejajan.view.main.parent.ui.student.change

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.data.model.NutritionModel
import com.mnrf.ejajan.data.model.SpendingModel
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

    private val _updateNutritionStatus = MutableLiveData<String>()
    val updateNutritionStatus: LiveData<String> get() = _updateNutritionStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _allergiesMap = MutableLiveData<Map<String, String>>() // ID -> Name
    val allergiesMap: LiveData<Map<String, String>> get() = _allergiesMap

    private val _spendingMap = MutableLiveData<Map<String, Pair<String, String>>>() // ID -> (Amount, Period)
    val spendingMap: LiveData<Map<String, Pair<String, String>>> get() = _spendingMap

    private val _nutritionMap = MutableLiveData<Map<String, Pair<String, String>>>() // ID -> (Amount, Period)
    val nutritionMap: LiveData<Map<String, Pair<String, String>>> get() = _nutritionMap

    // Fetch session details
    fun getSession(): Flow<UserModel> {
        return repository.getSession()
    }

    fun fetchAllergiesForStudent(studentUid: String, onSuccess: (List<AllergyModel>) -> Unit) {
        repository.getAllergies(
            studentUid = studentUid,
            onSuccess = { allergies -> onSuccess(allergies) },
            onFailure = { e -> _errorMessage.postValue("Error fetching allergies: ${e.message}") }
        )
    }

    fun fetchSpendingForStudent(studentUid: String, onSuccess: (List<SpendingModel>) -> Unit) {
        repository.getSpending(
            studentUid = studentUid,
            onSuccess = { spendings -> onSuccess(spendings) },
            onFailure = { e -> _errorMessage.postValue("Error fetching spending: ${e.message}") }
        )
    }

    fun fetchNutritionForStudent(studentUid: String, onSuccess: (List<NutritionModel>) -> Unit) {
        repository.getNutrition(
            studentUid = studentUid,
            onSuccess = { nutrition -> onSuccess(nutrition) },
            onFailure = { e -> _errorMessage.postValue("Error fetching nutrition: ${e.message}") }
        )
    }

    // Fetch allergies from repository
    fun fetchAllergies(studentUid: String) {
        repository.getAllergiesWithIds( studentUid,
            onSuccess = { data -> _allergiesMap.postValue(data) },
            onFailure = { e -> _errorMessage.postValue("Error fetching allergies: ${e.message}") }
        )
    }

    // Fetch spending constraints from repository
    fun fetchSpending(studentUid: String) {
        repository.getSpendingWithIds(studentUid,
            onSuccess = { data -> _spendingMap.postValue(data) },
            onFailure = { e -> _errorMessage.postValue("Error fetching spending data: ${e.message}") }
        )
    }

    fun fetchNutrition(studentUid: String) {
        repository.getNutritionWithIds(studentUid,
            onSuccess = { data -> _nutritionMap.postValue(data) },
            onFailure = { e -> _errorMessage.postValue("Error fetching nutrition data: ${e.message}") }
        )
    }

    // Update allergy constraint associated with the selected child's UID
    fun updateAllergy(studentUid: String, name: String, newName: String) {
        repository.updateAllergy(studentUid, name, newName)
            .addOnSuccessListener {
                _updateAllergyStatus.postValue("Allergy updated successfully")
            }
            .addOnFailureListener { exception ->
                _errorMessage.postValue("Error updating allergy: ${exception.message}")
            }
    }

    // Update spending constraint associated with the selected child's UID
    fun updateSpending(studentUid: String, spendingId: String, newAmount: String, newPeriod: String) {
        repository.updateSpending(studentUid, spendingId, newAmount, newPeriod)
            .addOnSuccessListener {
                _updateSpendingStatus.postValue("Spending updated successfully")
            }
            .addOnFailureListener { exception ->
                _errorMessage.postValue("Error updating spending: ${exception.message}")
            }
    }

    // Update nutrition constraint associated with the selected child's UID
    fun updateNutrition(studentUid: String, nutritionId: String, newName: String) {
        repository.updateNutrition(studentUid, nutritionId, newName)
            .addOnSuccessListener {
                _updateNutritionStatus.postValue("Nutrition updated successfully")
            }
            .addOnFailureListener { exception ->
                _errorMessage.postValue("Error updating nutrition: ${exception.message}")
            }
    }
}
