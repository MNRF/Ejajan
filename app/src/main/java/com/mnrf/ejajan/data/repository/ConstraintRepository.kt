package com.mnrf.ejajan.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.data.model.SpendingModel

class ConstraintRepository {
    private val db = FirebaseFirestore.getInstance()

    fun addAllergy(allergy: AllergyModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("allergies")
            .add(allergy)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getAllergies(onSuccess: (List<AllergyModel>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("allergies")
            .get()
            .addOnSuccessListener { snapshot ->
                val allergies = snapshot.documents.mapNotNull { it.toObject(AllergyModel::class.java) }
                onSuccess(allergies)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun addSpending(spending: SpendingModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("spendings")
            .add(spending)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

}