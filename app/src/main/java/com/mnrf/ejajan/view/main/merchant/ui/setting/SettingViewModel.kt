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
        if (daysOpen.lastOrNull() == '1') {
            val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val timeField = when (currentDay) {
                Calendar.MONDAY -> profile.start1
                Calendar.TUESDAY -> profile.start2
                Calendar.WEDNESDAY -> profile.start3
                Calendar.THURSDAY -> profile.start4
                Calendar.FRIDAY -> profile.start5
                Calendar.SATURDAY -> profile.start6
                Calendar.SUNDAY -> profile.start7
                else -> null
            }

            if (!timeField.isNullOrEmpty()) {
                setAlarm(timeField)
            } else {
                Log.e(TAG, "Time field is empty or null for the current day.")
            }
        } else {
            cancelAlarm()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun setAlarm(time: String) {
        try {
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            if (alarmManager == null) {
                Log.e(TAG, "AlarmManager is not available in the system.")
                return
            }

            // Check if the app can schedule exact alarms
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.e(TAG, "Exact alarm permission is not granted. Requesting permission.")
                requestExactAlarmPermission()
                return
            }

            val intent = Intent(appContext, AlarmReceiver::class.java).apply {
                action = "com.mnrf.ejajan.START_ALARM"
                putExtra("uid", _merchantProfile.value?.uid)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                appContext,
                ALARM_REQUEST_CODE,
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

            Log.d(TAG, "Exact alarm set for time: $time")
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
                ALARM_REQUEST_CODE,
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
        const val ALARM_REQUEST_CODE = 1001
    }
}




/*package com.mnrf.ejajan.view.main.merchant.ui.setting

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextClock
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.ActivityMerchantSettingScheduleBinding
import com.mnrf.ejajan.view.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class MerchantSettingSchedule : AppCompatActivity() {
    private lateinit var binding: ActivityMerchantSettingScheduleBinding
    private val settingViewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMerchantSettingScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupTimePickers()

        binding.toggleButton.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton.isChecked, 2)
            refresh()
        }
        binding.toggleButton2.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton2.isChecked, 3)
            refresh()
        }
        binding.toggleButton3.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton3.isChecked, 4)
            refresh()
        }
        binding.toggleButton4.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton4.isChecked, 5)
            refresh()
        }
        binding.toggleButton5.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton5.isChecked, 6)
            refresh()
        }
        binding.toggleButton6.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton6.isChecked, 7)
            refresh()
        }
        binding.toggleButton7.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton7.isChecked, 8)
            refresh()
        }
        binding.swcSchedule.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.swcSchedule.isChecked, 9)
            refresh()
        }

        refresh()
    }

    private fun setupTimePickers() {
        setupTimePicker(binding.textClock, "start1")
        setupTimePicker(binding.textClock2, "end1")
        setupTimePicker(binding.textClockTuesday, "start2")
        setupTimePicker(binding.textClockTuesday2, "end2")
        setupTimePicker(binding.textClockWednesday, "start3")
        setupTimePicker(binding.textClockWednesday2, "end3")
        setupTimePicker(binding.textClockThursday, "start4")
        setupTimePicker(binding.textClockThursday2, "end4")
        setupTimePicker(binding.textClockFriday, "start5")
        setupTimePicker(binding.textClockFriday2, "end5")
        setupTimePicker(binding.textClockSaturday, "start6")
        setupTimePicker(binding.textClockSaturday2, "end6")
        setupTimePicker(binding.textClockSunday, "start7")
        setupTimePicker(binding.textClockSunday2, "end7")
    }

    private fun setupTimePicker(textClock: TextClock, timeKey: String) {
        textClock.setOnClickListener {
            val currentTime = textClock.text.toString()
            val hour = currentTime.substring(0, 2).toIntOrNull() ?: 0
            val minute = currentTime.substring(3, 5).toIntOrNull() ?: 0

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val updatedTime = String.format("%02d%02d", selectedHour, selectedMinute)
                textClock.text = updatedTime
                settingViewModel.updateMerchantTime(timeKey, updatedTime)
            }, hour, minute, true).show()
        }
    }

    private fun refresh() {
        settingViewModel.merchantProfile.observe(this) { profile ->
            profile?.let {
                binding.textClock.text = formatTime(it.start1)
                binding.textClock2.text = formatTime(it.end1)
                binding.textClockTuesday.text = formatTime(it.start2)
                binding.textClockTuesday2.text = formatTime(it.end2)
                binding.textClockWednesday.text = formatTime(it.start3)
                binding.textClockWednesday2.text = formatTime(it.end3)
                binding.textClockThursday.text = formatTime(it.start4)
                binding.textClockThursday2.text = formatTime(it.end4)
                binding.textClockFriday.text = formatTime(it.start5)
                binding.textClockFriday2.text = formatTime(it.end5)
                binding.textClockSaturday.text = formatTime(it.start6)
                binding.textClockSaturday2.text = formatTime(it.end6)
                binding.textClockSunday.text = formatTime(it.start7)
                binding.textClockSunday2.text = formatTime(it.end7)
                if (binding.swcSchedule.isChecked) {
                    binding.tvMerchantScheduledopen.text =
                        getString(R.string.scheduled_open_is_active)
                } else {
                    binding.tvMerchantScheduledopen.text =
                        getString(R.string.scheduled_open_is_inactive)
                }
                settingViewModel.setupRepeatingAlarm()
            }
        }
    }

    private fun formatTime(time: String?): String {
        return try {
            val sdf = SimpleDateFormat("HHmm", Locale.getDefault())
            val parsedDate = sdf.parse(time ?: "0000")
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(parsedDate!!)
        } catch (e: Exception) {
            "00:00"
        }
    }

    companion object {
        const val MERCHANT_PROFILE = "merchant_profile"
    }
}
*/