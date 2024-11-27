package com.mnrf.ejajan.view.main.merchant.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mnrf.ejajan.databinding.FragmentMerchantSettingBinding
import com.mnrf.ejajan.view.login.LoginParentMerchant
import com.mnrf.ejajan.view.main.merchant.ui.setting.balance.TransactionDisbursementActivity
import com.mnrf.ejajan.view.main.merchant.ui.setting.profile.ProfileEditActivity
import com.mnrf.ejajan.view.main.merchant.ui.setting.report.ReportActivity

class SettingMerchantFragment : Fragment() {

    private var _binding: FragmentMerchantSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        binding.btnEdit.setOnClickListener {
            val intent = Intent(requireContext(), ProfileEditActivity::class.java)
            startActivity(intent)
        }

        binding.cvSettingCairkan.setOnClickListener {
            val intent = Intent(requireContext(), TransactionDisbursementActivity::class.java)
            startActivity(intent)
        }

        binding.cvSettingLaporan.setOnClickListener {
            val intent = Intent(requireContext(), ReportActivity::class.java)
            startActivity(intent)
        }

        binding.btSettingLogout.setOnClickListener {
            val intent = Intent(requireContext(), LoginParentMerchant::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}