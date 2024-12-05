package com.mnrf.ejajan.view.main.merchant.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.FragmentMerchantSettingBinding
import com.mnrf.ejajan.view.login.LoginParentMerchant
import com.mnrf.ejajan.view.main.merchant.ui.setting.balance.TransactionDisbursementActivity
import com.mnrf.ejajan.view.main.merchant.ui.setting.profile.ProfileEditActivity
import com.mnrf.ejajan.view.main.merchant.ui.setting.report.ReportActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory

class SettingMerchantFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentMerchantSettingBinding? = null
    private val binding get() = _binding!!
    private val settingViewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val scheduleRequestCode = 2001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMerchantSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.btnSettingLogout.setOnClickListener {
            auth.signOut()
            settingViewModel.logout()

            val intent = Intent(requireActivity(), LoginParentMerchant::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            requireActivity().finishAffinity()
        }

        binding.btnProfile.setOnClickListener {
            val intent = Intent(requireContext(), ProfileEditActivity::class.java)
            startActivity(intent)
        }

        binding.cvSettingCair.setOnClickListener {
            val intent = Intent(requireContext(), TransactionDisbursementActivity::class.java)
            startActivity(intent)
        }

        binding.cvSettingLaporan.setOnClickListener {
            val intent = Intent(requireContext(), ReportActivity::class.java)
            startActivity(intent)
        }

        binding.tbSettingJamBuka.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.toggleDaysopen(isChecked, 1)
            binding.tvSettingJamBuka.text = if (isChecked) {
                getString(R.string.toko_anda_sedang_buka)
            } else {
                getString(R.string.toko_anda_sedang_tutup)
            }
        }

        binding.btnSchedule.setOnClickListener {
            val intent = Intent(requireContext(), MerchantSettingSchedule::class.java)
            startActivityForResult(intent, scheduleRequestCode)
        }

        refresh()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == scheduleRequestCode && resultCode == Activity.RESULT_OK) {
            refresh() // Refresh data after returning from MerchantSettingSchedule activity
        }
    }

    private fun refresh() {
        settingViewModel.merchantProfile.observe(viewLifecycleOwner) { profile ->
            val isOpen = profile?.daysopen?.first() == '1'
            binding.tbSettingJamBuka.isChecked = isOpen
            binding.tvSettingJamBuka.text = if (isOpen) {
                getString(R.string.toko_anda_sedang_buka)
            } else {
                getString(R.string.toko_anda_sedang_tutup)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
