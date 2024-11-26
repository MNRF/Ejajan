package com.mnrf.ejajan.view.main.parent.ui.student

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mnrf.ejajan.databinding.FragmentParentStudentBinding
import com.mnrf.ejajan.view.main.parent.ui.student.add.AddConstraintActivity
import com.mnrf.ejajan.view.main.parent.ui.student.change.ChangeConstraintActivity
import com.mnrf.ejajan.view.main.parent.ui.student.delete.DeleteConstraintActivity

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnParentCreate.setOnClickListener {
            // Handle 'Tambah' button click
            val intent = Intent(requireContext(), AddConstraintActivity::class.java)
            startActivity(intent)
        }

        binding.btnParentUpdate.setOnClickListener {
            // Handle 'Ubah' button click
            val intent = Intent(requireContext(), ChangeConstraintActivity::class.java)
            startActivity(intent)
        }

        binding.btnParentDelete.setOnClickListener {
            // Handle 'Hapus' button click
            val intent = Intent(requireContext(), DeleteConstraintActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
