package com.mnrf.ejajan.view.main.parent.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mnrf.ejajan.data.pref.ThemePreferences
import com.mnrf.ejajan.databinding.FragmentParentSettingBinding
import com.mnrf.ejajan.view.login.LoginParentMerchant
import com.mnrf.ejajan.view.main.parent.ui.setting.notification.NotificationParentActivity
import com.mnrf.ejajan.view.main.parent.ui.setting.profile.ProfileParentActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory
import kotlin.system.exitProcess

class SettingParentFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
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

        auth = Firebase.auth



        // Set up logout button functionality
        binding.btnSettingLogout.setOnClickListener {
            auth.signOut() // Now auth is properly initialized
            setting2ViewModel.logout()

            val intent = Intent(requireActivity(), LoginParentMerchant::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            requireActivity().finishAffinity()
        }

        binding.btnProfile.setOnClickListener {
            // Tambahkan logika untuk tombol Edit Profile
            val intent = Intent(requireContext(), ProfileParentActivity::class.java)
            startActivity(intent)
        }

        setting2ViewModel.getThemeSetting().observe(viewLifecycleOwner) { isDarkMode ->
            binding.tbSettingDarkmode.isChecked = isDarkMode
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.tbSettingDarkmode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            setting2ViewModel.saveThemeSetting(isChecked)
        }

//        binding.cvSettingNotification.setOnClickListener {
//            // Tambahkan logika untuk pengaturan Notifikasi
//            val intent = Intent(requireContext(), NotificationParentActivity::class.java)
//            startActivity(intent)
//        }

        binding.cvSettingLanguage.setOnClickListener {
            // Tambahkan logika untuk pengaturan Bahasa
            startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
