package com.mnrf.ejajan.view.main.merchant.ui.setting

import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.widget.TextClock
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.model.MerchantProfileModel
import com.mnrf.ejajan.databinding.ActivityMerchantBinding
import com.mnrf.ejajan.databinding.ActivityMerchantSettingScheduleBinding
import com.mnrf.ejajan.view.utils.ViewModelFactory
import java.util.Locale

class MerchantSettingSchedule : AppCompatActivity() {
    private lateinit var binding: ActivityMerchantSettingScheduleBinding
    private val settingViewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMerchantSettingScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTimePickers()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toggleButton.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton.isChecked, 2)
        }
        binding.toggleButton2.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton2.isChecked, 3)
        }
        binding.toggleButton3.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton3.isChecked, 4)
        }
        binding.toggleButton4.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton4.isChecked, 5)
        }
        binding.toggleButton5.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton5.isChecked, 6)
        }
        binding.toggleButton6.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton6.isChecked, 7)
        }
        binding.toggleButton7.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.toggleButton7.isChecked, 8)
        }
        binding.swcSchedule.setOnClickListener {
            settingViewModel.toggleDaysopen(binding.swcSchedule.isChecked, 9)
            refresh()
        }
        refresh()
    }

    // Register ActivityResultCallback
    @RequiresApi(Build.VERSION_CODES.S)
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Trigger the refresh when coming back from MerchantSettingSchedule
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            refresh()  // This will refresh the UI when returning from the activity
        }
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

    @RequiresApi(Build.VERSION_CODES.S)
    private fun refresh() {
        settingViewModel.merchantProfile.observe(this) { profile ->
            val isActive0 = profile?.daysopen?.last() == '1'
            binding.swcSchedule.isChecked = isActive0
            binding.tvMerchantScheduledopen.text = if (isActive0) {
                getString(R.string.scheduled_open_is_active)
            } else {
                getString(R.string.scheduled_open_is_inactive)
            }

            binding.textClock.text = formatTime(profile?.start1)
            binding.textClock2.text = formatTime(profile?.end1)
            binding.textClockTuesday.text = formatTime(profile?.start2)
            binding.textClockTuesday2.text = formatTime(profile?.end2)
            binding.textClockWednesday.text = formatTime(profile?.start3)
            binding.textClockWednesday2.text = formatTime(profile?.end3)
            binding.textClockThursday.text = formatTime(profile?.start4)
            binding.textClockThursday2.text = formatTime(profile?.end4)
            binding.textClockFriday.text = formatTime(profile?.start5)
            binding.textClockFriday2.text = formatTime(profile?.end5)
            binding.textClockSaturday.text = formatTime(profile?.start6)
            binding.textClockSaturday2.text = formatTime(profile?.end6)
            binding.textClockSunday.text = formatTime(profile?.start7)
            binding.textClockSunday2.text = formatTime(profile?.end7)

            val isActive1 = profile?.daysopen?.get(1) == '1'
            binding.toggleButton.isChecked = isActive1

            val isActive2 = profile?.daysopen?.get(2) == '1'
            binding.toggleButton2.isChecked = isActive2

            val isActive3 = profile?.daysopen?.get(3) == '1'
            binding.toggleButton3.isChecked = isActive3

            val isActive4 = profile?.daysopen?.get(4) == '1'
            binding.toggleButton4.isChecked = isActive4

            val isActive5 = profile?.daysopen?.get(5) == '1'
            binding.toggleButton5.isChecked = isActive5

            val isActive6 = profile?.daysopen?.get(6) == '1'
            binding.toggleButton6.isChecked = isActive6

            val isActive7 = profile?.daysopen?.get(7) == '1'
            binding.toggleButton7.isChecked = isActive7

            val isActive8 = profile?.daysopen?.get(8) == '1'
            binding.swcSchedule.isChecked = isActive8

            if (binding.swcSchedule.isChecked) {
                binding.cardView3.setCardBackgroundColor(getResources().getColor(R.color.utama))
            } else {
                binding.cardView3.setCardBackgroundColor(getResources().getColor(R.color.gray))
            }
            settingViewModel.setupRepeatingAlarm()
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

    companion object{
        const val MERCHANT_PROFILE = "merchant_profile"
    }
}