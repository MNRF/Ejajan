package com.mnrf.ejajan.view.main.parent.ui.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mnrf.ejajan.databinding.FragmentParentStudentBinding

class StudentParentFragment : Fragment() {

    private var _binding: FragmentParentStudentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentParentStudentBinding.inflate(inflater, container, false)

        // Set up any views or listeners here
        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        // Example listeners for the buttons
        binding.btnParentCreate.setOnClickListener {
            // Handle 'Tambah' button click
        }

        binding.btnParentUpdate.setOnClickListener {
            // Handle 'Ubah' button click
        }

        binding.btnParentDelete.setOnClickListener {
            // Handle 'Hapus' button click
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
