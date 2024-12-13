package com.mnrf.ejajan.view.main.merchant.ui.setting

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        val action = intent.action
        val uid = intent.getStringExtra("uid")
        if (uid == null || action.isNullOrEmpty()) {
            Log.e(TAG, "Invalid action or UID.")
            return
        }

        when (action) {
            "com.mnrf.ejajan.START_ALARM" -> {
                Log.d(TAG, "Start alarm triggered for UID: $uid")
                toggleMerchantStatus(uid, true)
            }
            "com.mnrf.ejajan.STOP_ALARM" -> {
                Log.d(TAG, "Stop alarm triggered for UID: $uid")
                toggleMerchantStatus(uid, false)
            }
            else -> {
                Log.e(TAG, "Unrecognized action: $action")
            }
        }
    }


    private fun toggleMerchantStatus(uid: String, isActive: Boolean) {
        val db = Firebase.firestore

        db.collection("merchantprofiles")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentId = querySnapshot.documents[0].id
                    val daysopen = querySnapshot.documents[0].getString("daysopen") ?: ""

                    if (daysopen.isEmpty()) {
                        Log.e(TAG, "Daysopen field is empty or missing for UID: $uid")
                        return@addOnSuccessListener
                    }

                    // Update the first digit
                    val updatedDaysopen = if (isActive) {
                        "1${daysopen.substring(1)}"
                    } else {
                        "0${daysopen.substring(1)}"
                    }

                    db.collection("merchantprofiles").document(documentId)
                        .update("daysopen", updatedDaysopen)
                        .addOnSuccessListener {
                            Log.d(TAG, "Daysopen updated to: $updatedDaysopen for UID: $uid")
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Failed to update daysopen for UID: $uid", exception)
                        }
                } else {
                    Log.e(TAG, "No document found for UID: $uid")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error querying Firestore for UID: $uid", exception)
            }
    }


    private fun updateDaysOpen(daysopen: String, isActive: Boolean): String {
        val newStatus = if (isActive) '1' else '0'
        return newStatus + daysopen.substring(1)
    }

    companion object {
        const val TAG = "AlarmReceiver"
    }
}
