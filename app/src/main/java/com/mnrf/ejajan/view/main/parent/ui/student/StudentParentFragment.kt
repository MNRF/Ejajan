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
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.data.model.NutritionModel
import com.mnrf.ejajan.data.model.SpendingModel
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
        _binding = FragmentParentStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        val userPreference = UserPreference.getInstance(requireContext().dataStore)
        val repository = ConstraintRepository(userPreference)
        viewModel = ViewModelProvider(this, ConstraintViewModelFactory(repository))[StudentViewModel::class.java]

        // Observe data changes for UI updates
        viewModel.allergyList.observe(viewLifecycleOwner) { allergies ->
            val uniqueItems = allergies.distinctBy { it.student_uid }
            val displayItems = uniqueItems.map { allergy ->
                val childName = viewModel.parentsChildsList.value?.find { it.uid == allergy.student_uid }?.name ?: "Unknown"
                allergy to childName
            }
            allergyAdapter.updateItems(displayItems)
        }

        viewModel.spendingList.observe(viewLifecycleOwner) { spending ->
            val uniqueItems = spending.distinctBy { it.student_uid }
            val displayItems = uniqueItems.map { spendingItem ->
                val childName = viewModel.parentsChildsList.value?.find { it.uid == spendingItem.student_uid }?.name ?: "Unknown"
                spendingItem to childName
            }
            spendingAdapter.updateItems(displayItems)
        }

        viewModel.nutritionList.observe(viewLifecycleOwner) { nutrition ->
            val uniqueItems = nutrition.distinctBy { it.student_uid }
            val displayItems = uniqueItems.map { nutritionItem ->
                val childName = viewModel.parentsChildsList.value?.find { it.uid == nutritionItem.student_uid }?.name ?: "Unknown"
                nutritionItem to childName
            }
            nutritionAdapter.updateItems(displayItems)
        }

        // Get parent UID and fetch child UIDs
        val user = FirebaseAuth.getInstance().currentUser
        val parentUid = user?.uid ?: ""

        if (parentUid.isNotEmpty()) {
            viewModel.loadParentsChilds(parentUid)
            viewModel.parentsChildsList.observe(viewLifecycleOwner) { children ->
                val childUids = children.map { it.uid }
                if (childUids.isNotEmpty()) {
                    viewModel.loadAllergiesForChildren(childUids)
                    viewModel.loadSpendingForChildren(childUids)
                    viewModel.loadNutritionForChildren(childUids)
                }
            }
        }

        binding.btnParentCreate.setOnClickListener {
            val intent = Intent(requireContext(), AddConstraintActivity::class.java)
            startActivity(intent)
        }

        binding.btnParentUpdate.setOnClickListener {
            val intent = Intent(requireContext(), ChangeConstraintActivity::class.java)
            startActivity(intent)
        }

        binding.btnParentDelete.setOnClickListener {
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
