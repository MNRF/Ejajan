package com.mnrf.ejajan.view.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnrf.ejajan.databinding.StartToLoginBinding
import com.mnrf.ejajan.view.login.LoginParentMerchant
import com.mnrf.ejajan.view.login.LoginStudent
import com.mnrf.ejajan.view.utils.OnboardingPreferences

class StartToLogin : Fragment() {
    private var _binding: StartToLoginBinding? = null
    private val binding get() = _binding!!
    private var role: String? = null

    companion object {
        private const val ARG_ROLE = "role"

        @JvmStatic
        fun newInstance(role: String) = StartToLogin().apply {
            arguments = Bundle().apply {
                putString(ARG_ROLE, role)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StartToLoginBinding.inflate(inflater, container, false)
        role = arguments?.getString(ARG_ROLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartLogin.setOnClickListener {
            val onboardingPreferences = OnboardingPreferences(requireContext())
            onboardingPreferences.setOnboardingCompleted(true)
            role?.let { onboardingPreferences.setUserRole(it) }

            when (role) {
                "parent", "merchant" -> {
                    val intent = Intent(requireContext(), LoginParentMerchant::class.java)
                    startActivity(intent)
                }
                "student" -> {
                    val intent = Intent(requireContext(), LoginStudent::class.java)
                    startActivity(intent)
                }
            }
            requireActivity().finish()
        }
    }
}