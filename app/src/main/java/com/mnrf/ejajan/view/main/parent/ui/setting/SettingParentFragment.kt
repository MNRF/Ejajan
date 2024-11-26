package com.mnrf.ejajan.view.main.parent.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mnrf.ejajan.databinding.FragmentParentSettingBinding

class SettingParentFragment : Fragment() {

    private var _binding: FragmentParentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingViewModel = ViewModelProvider(this)[Setting2ViewModel::class.java]

        _binding = FragmentParentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root


        setupClickListeners()

        return root
    }

    private fun setupClickListeners() {
        binding.btnProfile.setOnClickListener {
            // Tambahkan logika untuk tombol Edit Profile
            // Contoh: Navigasi ke halaman edit profil
        }

        binding.cvSettingDarkmode.setOnClickListener {
            // Tambahkan logika untuk pengaturan Dark Mode
        }

        binding.cvSettingNotification.setOnClickListener {
            // Tambahkan logika untuk pengaturan Notifikasi
        }

        binding.cvSettingLanguage.setOnClickListener {
            // Tambahkan logika untuk pengaturan Bahasa
        }

        binding.btnSettingLogout.setOnClickListener {
            // Tambahkan logika untuk logout
            // Contoh: Menampilkan dialog konfirmasi
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
