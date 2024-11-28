package com.mnrf.ejajan.view.main.merchant.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mnrf.ejajan.databinding.FragmentMerchantSettingBinding
import com.mnrf.ejajan.view.login.LoginParentMerchant
import com.mnrf.ejajan.view.main.merchant.ui.setting.balance.TransactionDisbursementActivity
import com.mnrf.ejajan.view.main.merchant.ui.setting.profile.ProfileEditActivity
import com.mnrf.ejajan.view.main.merchant.ui.setting.report.ReportActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory

class SettingMerchantFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentMerchantSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val settingViewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}