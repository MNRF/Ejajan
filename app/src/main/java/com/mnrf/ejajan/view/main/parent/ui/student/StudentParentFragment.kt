package com.mnrf.ejajan.view.main.parent.ui.student

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.mnrf.ejajan.data.pref.UserPreference
import com.mnrf.ejajan.data.pref.dataStore
import com.mnrf.ejajan.data.repository.ConstraintRepository
import com.mnrf.ejajan.databinding.FragmentParentStudentBinding
import com.mnrf.ejajan.view.main.parent.ui.student.add.AddConstraintActivity
import com.mnrf.ejajan.view.main.parent.ui.student.add.AddConstraintViewModel
import com.mnrf.ejajan.view.main.parent.ui.student.adapter.AllergyAdapter
import com.mnrf.ejajan.view.main.parent.ui.student.adapter.NutritionAdapter
import com.mnrf.ejajan.view.main.parent.ui.student.adapter.SpendingAdapter
import com.mnrf.ejajan.view.main.parent.ui.student.change.ChangeConstraintActivity
import com.mnrf.ejajan.view.main.parent.ui.student.delete.DeleteConstraintActivity

class StudentParentFragment : Fragment() {

    private var _binding: FragmentParentStudentBinding? = null
    private val binding get() = _binding!!
    private lateinit var allergyAdapter: AllergyAdapter
    private lateinit var spendingAdapter: SpendingAdapter
    private lateinit var nutritionAdapter: NutritionAdapter
    private lateinit var viewModel: StudentViewModel

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

        setupRecyclerView()

        val userPreference = UserPreference.getInstance(requireContext().dataStore)
        val repository = ConstraintRepository(userPreference)
        viewModel = ViewModelProvider(this, ConstraintViewModelFactory(repository))[StudentViewModel::class.java]

        viewModel.allergyList.observe(viewLifecycleOwner) { allergies ->
            allergyAdapter.updateItems(allergies)
        }

        viewModel.spendingList.observe(viewLifecycleOwner) { spending ->
            spendingAdapter.updateItems(spending)
        }

        viewModel.nutritionList.observe(viewLifecycleOwner) { nutrition ->
            nutritionAdapter.updateItems(nutrition)
        }

        // Dapatkan UID pengguna
        val user = FirebaseAuth.getInstance().currentUser
        val parentUid = user?.uid ?: ""

        // Muat data alergi dan pengeluaran berdasarkan parentUid
        if (parentUid.isNotEmpty()) {
            viewModel.loadAllergies(parentUid)
            viewModel.loadSpending(parentUid)
            viewModel.loadNutrition(parentUid)
        }
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

    private fun setupRecyclerView() {
        allergyAdapter = AllergyAdapter(mutableListOf())
        binding.rvAlergi.apply {
            adapter = allergyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        spendingAdapter = SpendingAdapter(mutableListOf())
        binding.rvSpending.apply {
            adapter = spendingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        nutritionAdapter = NutritionAdapter(mutableListOf())
        binding.rvNutrition.apply {
            adapter = nutritionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
