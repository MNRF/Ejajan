package com.mnrf.ejajan.view.main.parent.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mnrf.ejajan.databinding.FragmentParentSettingBinding
import com.mnrf.ejajan.view.login.LoginParentMerchant
import com.mnrf.ejajan.view.main.parent.ui.setting.notification.NotificationParentActivity
import com.mnrf.ejajan.view.main.parent.ui.setting.profile.ProfileParentActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory

class SettingParentFragment : Fragment() {

    private var _binding: FragmentParentSettingBinding? = null
    private val binding get() = _binding!!

    private val setting2ViewModel: Setting2ViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParentSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnProfile.setOnClickListener {
            // Tambahkan logika untuk tombol Edit Profile
            val intent = Intent(requireContext(), ProfileParentActivity::class.java)
            startActivity(intent)
        }

        binding.cvSettingDarkmode.setOnClickListener {
            // Tambahkan logika untuk pengaturan Dark Mode
        }

        binding.cvSettingNotification.setOnClickListener {
            // Tambahkan logika untuk pengaturan Notifikasi
            val intent = Intent(requireContext(), NotificationParentActivity::class.java)
            startActivity(intent)
        }

        binding.cvSettingLanguage.setOnClickListener {
            // Tambahkan logika untuk pengaturan Bahasa
            startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.btnSettingLogout.setOnClickListener {
            // Tambahkan logika untuk logout
            setting2ViewModel.logout()
            val intent = Intent(requireContext(), LoginParentMerchant::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
