package com.mnrf.ejajan.view.main.parent.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.mnrf.ejajan.databinding.FragmentSetting2Binding
import com.mnrf.ejajan.databinding.FragmentSettingBinding

class Setting2Fragment : Fragment() {

    private var _binding: FragmentSetting2Binding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingViewModel = ViewModelProvider(this)[Setting2ViewModel::class.java]
        _binding = FragmentSetting2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSetting2
        settingViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}