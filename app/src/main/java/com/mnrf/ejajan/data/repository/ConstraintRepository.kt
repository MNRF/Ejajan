package com.mnrf.ejajan.data.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.data.model.NutritionModel
import com.mnrf.ejajan.data.model.SpendingModel
import com.mnrf.ejajan.data.model.StudentProfileModel
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
            "student_uid" to allergy.student_uid,
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
            "student_uid" to spending.student_uid,
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
        val nutritionData = hashMapOf(
            "student_uid" to nutrition.student_uid,
            "name" to nutrition.name
        )

        db.collection("nutrition")
            .add(nutritionData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                onFailure(e)
            }
    }

    fun updateAllergy(studentUid: String, name: String, newName: String): Task<Void> {
        val query = db.collection("allergies")
            .whereEqualTo("student_uid", studentUid)
            .whereEqualTo("name", name)

        return query.get().continueWithTask { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot != null && !snapshot.isEmpty) {
                    val documentId = snapshot.documents.first().id
                    return@continueWithTask db.collection("allergies")
                        .document(documentId)
                        .update(
                            "name", newName
                        )
                } else {
                    throw Exception("No allergy found for studentUid: $studentUid and name: $name")
                }
            } else {
                throw task.exception ?: Exception("Error fetching allergy documents")
            }
        }
    }


    fun updateSpending(
        studentUid: String,
        amount: String,
        newAmount: String,
        newPeriod: String
    ): Task<Void> {
        val query = db.collection("spending")
            .whereEqualTo("student_uid", studentUid)
            .whereEqualTo("amount", amount)

        return query.get().continueWithTask { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot != null && !snapshot.isEmpty) {
                    val documentId = snapshot.documents.first().id
                    return@continueWithTask db.collection("spending")
                        .document(documentId)
                        .update(
                            "amount", newAmount,
                            "period", newPeriod
                        )
                } else {
                    throw Exception("No spending found for studentUid: $studentUid and amount: $amount")
                }
            } else {
                throw task.exception ?: Exception("Error fetching spending documents")
            }
        }
        /*return db.collection("spending")
            .document(spendingId)
            .update(
                "amount", newAmount,
                "period", newPeriod,
                "student_uid", studentUid
            )*/
    }

    fun updateNutrition(studentUid: String, name: String, newName: String): Task<Void> {
        val query = db.collection("nutrition")
            .whereEqualTo("student_uid", studentUid)
            .whereEqualTo("name", name)

        return query.get().continueWithTask { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot != null && !snapshot.isEmpty) {
                    val documentId = snapshot.documents.first().id
                    return@continueWithTask db.collection("nutrition")
                        .document(documentId)
                        .update(
                            "name", newName,
                        )
                } else {
                    throw Exception("No nutrition found for studentUid: $studentUid and name: $name")
                }
            } else {
                throw task.exception ?: Exception("Error fetching nutrition documents")
            }
        }
        /*return db.collection("nutrition")
            .document(nutritionId)
            .update(
                "name", newName,
                "student_uid", studentUid
            )*/
    }

    fun getParentsChilds(
        parentUid: String,
        onSuccess: (List<StudentProfileModel>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("studentprofiles")
            .whereEqualTo("parent_uid", parentUid)
            .get()
            .addOnSuccessListener { snapshot ->
                val parentsChilds = snapshot.documents.mapNotNull { it.toObject(StudentProfileModel::class.java) }
                onSuccess(parentsChilds)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getAllergies(
        studentUid: String,
        onSuccess: (List<AllergyModel>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("allergies")
            .whereEqualTo("student_uid", studentUid)
            .get()
            .addOnSuccessListener { snapshot ->
                val allergies = snapshot.documents.mapNotNull { it.toObject(AllergyModel::class.java) }
                onSuccess(allergies)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getSpending(
        studentUid: String,
        onSuccess: (List<SpendingModel>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("spending")
            .whereEqualTo("student_uid", studentUid)
            .get()
            .addOnSuccessListener { snapshot ->
                val spending = snapshot.documents.mapNotNull { it.toObject(SpendingModel::class.java) }
                onSuccess(spending)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getNutrition(
        studentUid: String,
        onSuccess: (List<NutritionModel>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("nutrition")
            .whereEqualTo("student_uid", studentUid)
            .get()
            .addOnSuccessListener { snapshot ->
                val nutrition = snapshot.documents.mapNotNull { it.toObject(NutritionModel::class.java) }
                onSuccess(nutrition)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getAllergiesWithIds(studentUid: String, onSuccess: (Map<String, String>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("allergies").whereEqualTo("student_uid", studentUid)
            .get()
            .addOnSuccessListener { snapshot ->
                val allergies = snapshot.documents.associate {
                    it.id to (it.getString("name") ?: "")
                }
                onSuccess(allergies)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getSpendingWithIds(studentUid: String, onSuccess: (Map<String, Pair<String, String>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("spending").whereEqualTo("student_uid", studentUid)
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

    fun getNutritionWithIds(studentUid: String, onSuccess: (Map<String, Pair<String, String>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("nutrition").whereEqualTo("student_uid", studentUid)
            .get()
            .addOnSuccessListener { snapshot ->
                val nutrition = snapshot.documents.associate {
                    it.id to Pair(
                        it.getString("name") ?: "",
                        /*it.getString("mineral") ?: */""
                    )
                }
                onSuccess(nutrition)
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

    fun updateNutrition(allergiesId: String, newName: String/*, newMineral: String*/): Task<Void> {
        return db.collection("nutrition").document(allergiesId).update(
            "name", newName/*,
            *//*"mineral", newMineral*/
        )
    }

    companion object {
        const val TAG = "constraint"
    }
}