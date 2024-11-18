package com.mnrf.ejajan.view.main.parent.ui.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mnrf.ejajan.databinding.FragmentStudentBinding

class StudentFragment : Fragment() {

    private var _binding: FragmentStudentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val studentViewModel =
            ViewModelProvider(this).get(StudentViewModel::class.java)

        _binding = FragmentStudentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textStudent
        studentViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}