package com.mnrf.ejajan.view.main.parent.ui.student

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.data.model.NutritionModel
import com.mnrf.ejajan.data.model.SpendingModel
import com.mnrf.ejajan.data.model.StudentProfileModel
import com.mnrf.ejajan.data.pref.UserPreference
import com.mnrf.ejajan.data.repository.ConstraintRepository
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: ConstraintRepository
) : ViewModel() {
    private val _allergyList = MutableLiveData<List<AllergyModel>>()
    val allergyList: LiveData<List<AllergyModel>> get() = _allergyList

    private val _spendingList = MutableLiveData<List<SpendingModel>>()
    val spendingList: LiveData<List<SpendingModel>> get() = _spendingList

    private val _nutritionList = MutableLiveData<List<NutritionModel>>()
    val nutritionList: LiveData<List<NutritionModel>> get() = _nutritionList

    private val _parentsChildsList = MutableLiveData<List<StudentProfileModel>>()
    val parentsChildsList: LiveData<List<StudentProfileModel>> get() = _parentsChildsList

    fun loadParentsChilds(parentUid: String) {
        repository.getParentsChilds(parentUid, { parentsChilds ->
            _parentsChildsList.value = parentsChilds
        }, { e ->
            Log.e("StudentViewModel", "Failed to load allergies: ${e.message}")
        })
    }

    fun loadAllergiesForChildren(childUids: List<String>) {
        _allergyList.value = emptyList() // Clear the existing list
        childUids.forEach { childUid ->
            repository.getAllergies(childUid, { allergies ->
                _allergyList.value = (_allergyList.value ?: emptyList()) + allergies
            }, { e ->
                Log.e("StudentViewModel", "Failed to load allergies for $childUid: ${e.message}")
            })
        }
    }

    fun loadSpendingForChildren(childUids: List<String>) {
        _spendingList.value = emptyList() // Clear the existing list
        childUids.forEach { childUid ->
            repository.getSpending(childUid, { spending ->
                _spendingList.value = (_spendingList.value ?: emptyList()) + spending
            }, { e ->
                Log.e("StudentViewModel", "Failed to load spending for $childUid: ${e.message}")
            })
        }
    }

    fun loadNutritionForChildren(childUids: List<String>) {
        _nutritionList.value = emptyList() // Clear the existing list
        childUids.forEach { childUid ->
            repository.getNutrition(childUid, { nutrition ->
                _nutritionList.value = (_nutritionList.value ?: emptyList()) + nutrition
            }, { e ->
                Log.e("StudentViewModel", "Failed to load nutrition for $childUid: ${e.message}")
            })
        }
    }



/*    fun loadAllergies(parentUid: String) {
        repository.getAllergies(parentUid, { allergies ->
            _allergyList.value = allergies
        }, { e ->
            Log.e("StudentViewModel", "Failed to load allergies: ${e.message}")
        })
    }

    fun loadSpending(parentUid: String) {
        repository.getSpending(parentUid, { spending ->
            _spendingList.value = spending
        }, { e ->
            Log.e("StudentViewModel", "Failed to load spending: ${e.message}")
        })
    }

    fun loadNutrition(parentUid: String) {
        repository.getNutrition(parentUid, { nutrition ->
            _nutritionList.value = nutrition
        }, { e ->
            Log.e("StudentViewModel", "Failed to load spending: ${e.message}")
        })
    }*/
}