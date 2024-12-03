package com.mnrf.ejajan.data.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.data.model.NutritionModel
import com.mnrf.ejajan.data.model.SpendingModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class ConstraintRepository (
    private val userPreference: UserPreference,
) {
    private val db = Firebase.firestore

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun addAllergy(allergy: AllergyModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val allergyData = hashMapOf(
            "parent_uid" to allergy.parentUid,
            "name" to allergy.name
        )

        db.collection("allergies")
            .add(allergyData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                onFailure(e)
            }
    }

    fun addSpending(
        spending: SpendingModel,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val spendingData = hashMapOf(
            "parent_uid" to spending.parentUid,
            "amount" to spending.amount,
            "period" to spending.period
        )

        db.collection("spending")
            .add(spendingData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                onFailure(e)
            }
    }

    fun addNutrition(nutrition: NutritionModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val allergyData = hashMapOf(
            "parent_uid" to nutrition.parentUid,
            "name" to nutrition.name,
            "mineral" to nutrition.mineral
        )

        db.collection("nutrition")
            .add(allergyData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                onFailure(e)
            }
    }

    fun getAllergies(
        parentUid: String,
        onSuccess: (List<AllergyModel>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("allergies")
            .whereEqualTo("parent_uid", parentUid)
            .get()
            .addOnSuccessListener { snapshot ->
                val allergies = snapshot.documents.mapNotNull { it.toObject(AllergyModel::class.java) }
                onSuccess(allergies)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getSpending(
        parentUid: String,
        onSuccess: (List<SpendingModel>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("spending")
            .whereEqualTo("parent_uid", parentUid)
            .get()
            .addOnSuccessListener { snapshot ->
                val spending = snapshot.documents.mapNotNull { it.toObject(SpendingModel::class.java) }
                onSuccess(spending)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getNutrition(
        parentUid: String,
        onSuccess: (List<AllergyModel>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("nutrition")
            .whereEqualTo("parent_uid", parentUid)
            .get()
            .addOnSuccessListener { snapshot ->
                val allergies = snapshot.documents.mapNotNull { it.toObject(AllergyModel::class.java) }
                onSuccess(allergies)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getAllergiesWithIds(onSuccess: (Map<String, String>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("allergies")
            .get()
            .addOnSuccessListener { snapshot ->
                val allergies = snapshot.documents.associate {
                    it.id to (it.getString("name") ?: "")
                }
                onSuccess(allergies)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getSpendingWithIds(onSuccess: (Map<String, Pair<String, String>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("spending")
            .get()
            .addOnSuccessListener { snapshot ->
                val spendings = snapshot.documents.associate {
                    it.id to Pair(
                        it.getString("amount") ?: "",
                        it.getString("period") ?: ""
                    )
                }
                onSuccess(spendings)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getNutritionWithIds(onSuccess: (Map<String, String>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("nutrition")
            .get()
            .addOnSuccessListener { snapshot ->
                val allergies = snapshot.documents.associate {
                    it.id to (it.getString("name") ?: "")
                    it.id to (it.getString("mineral") ?: "")
                }
                onSuccess(allergies)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun deleteAllergyById(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("allergies").document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    fun deleteSpendingById(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("spending").document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    fun deleteNutritionById(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("nutrition").document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    fun updateAllergy(allergiesId: String, newName: String): Task<Void> {
        return db.collection("allergies").document(allergiesId).update(
            "name", newName
        )
    }

    fun updateSpending(spendingId: String, newAmount: String, newPeriod: String): Task<Void> {
        return db.collection("spending").document(spendingId).update(
            "amount", newAmount,
            "period", newPeriod
        )
    }

    fun updateNutrition(allergiesId: String, newName: String, newMineral: String): Task<Void> {
        return db.collection("nutrition").document(allergiesId).update(
            "name", newName,
            "mineral", newMineral
        )
    }

    companion object {
        const val TAG = "constraint"
    }
}