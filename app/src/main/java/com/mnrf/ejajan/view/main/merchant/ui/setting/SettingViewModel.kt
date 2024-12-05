package com.mnrf.ejajan.view.main.merchant.ui.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.data.model.MerchantProfileModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.repository.UserRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: UserRepository,
                       private val appContext: Context) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _merchantProfile = MutableLiveData<MerchantProfileModel?>()
    val merchantProfile: LiveData<MerchantProfileModel?> = _merchantProfile

    private val db = Firebase.firestore

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    init {
        fetchMerchantProfile()
    }

    fun fetchMerchantProfile() {
        _isLoading.value = true
        getSession().observeForever { user ->
            user?.let {
                db.collection("merchantprofiles")
                    .whereEqualTo("uid", user.token)
                    .get()
                    .addOnSuccessListener { result ->
                        val profiles = result.documents.mapNotNull { doc ->
                            doc.toObject(MerchantProfileModel::class.java)
                        }
                        _merchantProfile.value = profiles.firstOrNull()
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error fetching merchant profile", exception)
                        _isLoading.value = false
                    }
            }
        }
    }

    fun toggleDaysopen(isActive: Boolean, position: Short) {
        val updatedProfile = _merchantProfile.value ?: return
        val daysOpen = updatedProfile.daysopen
        if (position < 1 || position > 9) {
            Log.e(TAG, "Invalid position: $position. Must be between 1 and 9.")
            return
        }
        val updatedDaysOpen = daysOpen.mapIndexed { index, char ->
            if (index == position - 1) {
                if (isActive) '1' else '0'
            } else {
                char
            }
        }.joinToString("")
        db.collection("merchantprofiles").whereEqualTo("uid", updatedProfile.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentId = querySnapshot.documents[0].id
                    db.collection("merchantprofiles").document(documentId)
                        .update("daysopen", updatedDaysOpen)
                        .addOnSuccessListener {
                            _merchantProfile.value = updatedProfile.copy(daysopen = updatedDaysOpen)
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Error updating merchant open/close status", exception)
                        }
                } else {
                    Log.e(TAG, "No document found that contains uid: ${updatedProfile.uid}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error querying merchant profile", exception)
            }
    }

    fun updateMerchantTime(field: String, timeValue: String) {
        val updatedProfile = _merchantProfile.value ?: return
        val uid = updatedProfile.uid

        db.collection("merchantprofiles").whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentId = querySnapshot.documents[0].id
                    db.collection("merchantprofiles").document(documentId)
                        .update(field, timeValue)
                        .addOnSuccessListener {
                            _merchantProfile.value = when (field) {
                                "start1" -> updatedProfile.copy(start1 = timeValue)
                                "end1" -> updatedProfile.copy(end1 = timeValue)
                                "start2" -> updatedProfile.copy(start2 = timeValue)
                                "end2" -> updatedProfile.copy(end2 = timeValue)
                                "start3" -> updatedProfile.copy(start3 = timeValue)
                                "end3" -> updatedProfile.copy(end3 = timeValue)
                                "start4" -> updatedProfile.copy(start4 = timeValue)
                                "end4" -> updatedProfile.copy(end4 = timeValue)
                                "start5" -> updatedProfile.copy(start5 = timeValue)
                                "end5" -> updatedProfile.copy(end5 = timeValue)
                                "start6" -> updatedProfile.copy(start6 = timeValue)
                                "end6" -> updatedProfile.copy(end6 = timeValue)
                                "start7" -> updatedProfile.copy(start7 = timeValue)
                                "end7" -> updatedProfile.copy(end7 = timeValue)
                                else -> updatedProfile
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Error updating merchant time", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error querying merchant profile", exception)
            }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun setupRepeatingAlarm() {
        val profile = _merchantProfile.value ?: return
        val daysOpen = profile.daysopen

        // Ensure no alarms are set if the last digit of `daysopen` is '0'
        if (daysOpen.lastOrNull() != '1') {
            cancelAlarm()
            Log.d(TAG, "Alarms canceled as the last digit of daysopen is not active (0).")
            return
        }

        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        // Fetch start and end times based on the current day
        val startField = when (currentDay) {
            Calendar.MONDAY -> profile.start1
            Calendar.TUESDAY -> profile.start2
            Calendar.WEDNESDAY -> profile.start3
            Calendar.THURSDAY -> profile.start4
            Calendar.FRIDAY -> profile.start5
            Calendar.SATURDAY -> profile.start6
            Calendar.SUNDAY -> profile.start7
            else -> null
        }
        val endField = when (currentDay) {
            Calendar.MONDAY -> profile.end1
            Calendar.TUESDAY -> profile.end2
            Calendar.WEDNESDAY -> profile.end3
            Calendar.THURSDAY -> profile.end4
            Calendar.FRIDAY -> profile.end5
            Calendar.SATURDAY -> profile.end6
            Calendar.SUNDAY -> profile.end7
            else -> null
        }

        // Set alarms for both start and end times
        if (!startField.isNullOrEmpty()) {
            setAlarm(startField, isStart = true)
        } else {
            Log.e(TAG, "Start time field is empty or null for the current day.")
        }

        if (!endField.isNullOrEmpty()) {
            setAlarm(endField, isStart = false)
        } else {
            Log.e(TAG, "End time field is empty or null for the current day.")
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun setAlarm(time: String, isStart: Boolean) {
        try {
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            if (alarmManager == null) {
                Log.e(TAG, "AlarmManager is not available in the system.")
                return
            }

            if (!alarmManager.canScheduleExactAlarms()) {
                Log.e(TAG, "Exact alarm permission is not granted. Requesting permission.")
                requestExactAlarmPermission()
                return
            }

            val action = if (isStart) "com.mnrf.ejajan.START_ALARM" else "com.mnrf.ejajan.STOP_ALARM"
            val intent = Intent(appContext, AlarmReceiver::class.java).apply {
                this.action = action
                putExtra("uid", _merchantProfile.value?.uid)
            }
            val requestCode = if (isStart) ALARM_REQUEST_CODE_START else ALARM_REQUEST_CODE_END
            val pendingIntent = PendingIntent.getBroadcast(
                appContext,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = Calendar.getInstance().apply {
                val hour = time.substring(0, 2).toInt()
                val minute = time.substring(2, 4).toInt()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    // Schedule for the next day if time is in the past
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            Log.d(TAG, "Exact alarm set for ${if (isStart) "start" else "end"} time: $time")
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to set exact alarm due to missing permission: ${e.message}")
            requestExactAlarmPermission()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to set alarm: ${e.message}")
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestExactAlarmPermission() {
        try {
            val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.data = Uri.parse("package:${appContext.packageName}")
            appContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            Log.d(TAG, "Prompting user to grant exact alarm permission.")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to launch permission request: ${e.message}")
        }
    }


    fun cancelAlarm() {
        try {
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            if (alarmManager == null) {
                Log.e(TAG, "AlarmManager is not available in the system.")
                return
            }

            val intent = Intent(appContext, AlarmReceiver::class.java).apply {
                action = "com.mnrf.ejajan.STOP_ALARM"
            }
            val pendingIntent = PendingIntent.getBroadcast(
                appContext,
                ALARM_REQUEST_CODE_END,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.cancel(pendingIntent)
            Log.d(TAG, "Alarm canceled successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cancel alarm: ${e.message}")
        }
    }


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    companion object {
        const val TAG = "SettingViewModel"
        const val ALARM_REQUEST_CODE_START = 1001
        const val ALARM_REQUEST_CODE_END = 1002
    }
}
