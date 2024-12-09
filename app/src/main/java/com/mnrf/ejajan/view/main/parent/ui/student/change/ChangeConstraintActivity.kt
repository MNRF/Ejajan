package com.mnrf.ejajan.view.main.parent.ui.student.change

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.model.StudentProfileModel
import com.mnrf.ejajan.data.pref.UserPreference
import com.mnrf.ejajan.data.pref.dataStore
import com.mnrf.ejajan.data.repository.ConstraintRepository
import com.mnrf.ejajan.databinding.ActivityParentChangeBinding
import com.mnrf.ejajan.view.main.parent.ParentActivity
import com.mnrf.ejajan.view.main.parent.ui.student.ConstraintViewModelFactory
import com.mnrf.ejajan.view.main.parent.ui.student.StudentViewModel

class ChangeConstraintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentChangeBinding
    private val viewModel: ChangeConstraintViewModel by viewModels {
        ConstraintViewModelFactory(
            repository = ConstraintRepository(UserPreference.getInstance(this.dataStore)),
        )
    }

    private val studentViewModel: StudentViewModel by viewModels {
        ConstraintViewModelFactory(
            repository = ConstraintRepository(UserPreference.getInstance(this.dataStore)),
        )
    }

    private lateinit var selectedChild: StudentProfileModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Change Constraint"
        }

        loadParentsChildren() // Load the children into spinner
        setupObservers()
        setupListeners()
    }

    private fun loadParentsChildren() {
        val parentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        studentViewModel.loadParentsChilds(parentUid)

        studentViewModel.parentsChildsList.observe(this) { childs ->
            val childNames = childs.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, childNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spNamaAnak.adapter = adapter

            binding.spNamaAnak.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        selectedChild = childs[position]
                        loadConstraintsForSelectedChild() // Load constraints specific to this child
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // No action
                    }
                }
        }
    }

/*    private fun loadConstraintsForSelectedChild() {
        val studentUid = selectedChild.uid

        setupSpinner(binding.spConstraint, R.array.Constraint)
        setupSpinner(binding.spChangeAlergi, R.array.Alergi)
        setupSpinner(binding.spChangeLimit, R.array.Pengeluaran)
        setupSpinner(binding.spChangePeriod, R.array.Waktu)
        setupSpinner(binding.spChangeNutrition, R.array.Nutrisi)

        // Fetch and filter constraints for the selected child
        viewModel.fetchAllergiesForStudent(studentUid) { allergies ->
            setupSpinnerData(binding.spAlergi, allergies.map { it.name })
        }

        viewModel.fetchSpendingForStudent(studentUid) { spendings ->
            setupSpinnerData(binding.spLimit, spendings.map { it.amount })
            setupSpinnerData(binding.spPeriod, spendings.map { it.period })
        }

        viewModel.fetchNutritionForStudent(studentUid) { nutrition ->
            setupSpinnerData(binding.spNutrition, nutrition.map { it.name })
        }
    }*/

    private fun loadConstraintsForSelectedChild() {
        val studentUid = selectedChild.uid

        setupSpinner(binding.spConstraint, R.array.Constraint)
        setupSpinner(binding.spChangeAlergi, R.array.Alergi)
        setupSpinner(binding.spChangeLimit, R.array.Pengeluaran)
        setupSpinner(binding.spChangePeriod, R.array.Waktu)
        setupSpinner(binding.spChangeNutrition, R.array.Nutrisi)

        // Fetch and filter constraints for the selected child
        viewModel.fetchAllergiesForStudent(studentUid) { allergies ->
            setupSpinnerData(binding.spAlergi, allergies.map { it.name })
        }

        viewModel.fetchSpendingForStudent(studentUid) { spendings ->
            setupSpinnerData(binding.spLimit, spendings.map { it.amount })
            setupSpinnerData(binding.spPeriod, spendings.map { it.period })
        }

        viewModel.fetchNutritionForStudent(studentUid) { nutrition ->
            setupSpinnerData(binding.spNutrition, nutrition.map { it.name })
        }
    }


    private fun setupObservers() {
        viewModel.updateAllergyStatus.observe(this) { statusMessage ->
            statusMessage?.let { showSuccessDialog(it) }
        }

        viewModel.updateSpendingStatus.observe(this) { statusMessage ->
            statusMessage?.let { showSuccessDialog(it) }
        }

        viewModel.updateNutritionStatus.observe(this) { statusMessage ->
            statusMessage?.let { showSuccessDialog(it) }
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let { showErrorToast(it) }
        }
    }

    private fun setupListeners() {
        binding.spConstraint.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedConstraint = binding.spConstraint.selectedItem.toString()
                toggleConstraintViews(selectedConstraint)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        binding.btnChange.setOnClickListener {
            if (!::selectedChild.isInitialized) {
                showErrorToast("Please select a child first.")
                return@setOnClickListener
            }

            val selectedConstraint = binding.spConstraint.selectedItem.toString()
            when (selectedConstraint) {
                "Alergi" -> handleUpdateAllergy()
                "Pengeluaran" -> handleUpdateSpending()
                "Nutrisi" -> handleUpdateNutrition()
            }
        }

        binding.infoIcon.setOnClickListener { showInfoDialog() }
    }

    private fun setupSpinner(spinner: Spinner, arrayResId: Int) {
        val items = resources.getStringArray(arrayResId)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupSpinnerData(spinner: Spinner, items: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun showSuccessDialog(message: String) {
        if (!isFinishing && !isDestroyed) {
            AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showInfoDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Constraint Information")
            .setMessage("The parent chooses the child and the constraint to change.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun toggleConstraintViews(selectedConstraint: String) {
        when (selectedConstraint) {
            "Alergi" -> {
                binding.tvAlergi.visibility = View.VISIBLE
                binding.spAlergi.visibility = View.VISIBLE
                binding.tvChangeAlergi.visibility = View.VISIBLE
                binding.spChangeAlergi.visibility = View.VISIBLE
                binding.tvChange.visibility = View.VISIBLE

                binding.tvLimit.visibility = View.GONE
                binding.spLimit.visibility = View.GONE
                binding.tvPeriod.visibility = View.GONE
                binding.spPeriod.visibility = View.GONE
                binding.tvChangeLimit.visibility = View.GONE
                binding.spChangeLimit.visibility = View.GONE
                binding.tvChangePeriod.visibility = View.GONE
                binding.spChangePeriod.visibility = View.GONE
                binding.tvNutrition.visibility = View.GONE
                binding.spNutrition.visibility = View.GONE
                binding.tvChangeNutrition.visibility = View.GONE
                binding.spChangeNutrition.visibility = View.GONE
            }
            "Pengeluaran" -> {
                binding.tvLimit.visibility = View.VISIBLE
                binding.spLimit.visibility = View.VISIBLE
                binding.tvPeriod.visibility = View.VISIBLE
                binding.spPeriod.visibility = View.VISIBLE
                binding.tvChangeLimit.visibility = View.VISIBLE
                binding.spChangeLimit.visibility = View.VISIBLE
                binding.tvChangePeriod.visibility = View.VISIBLE
                binding.spChangePeriod.visibility = View.VISIBLE
                binding.tvChange.visibility = View.VISIBLE

                binding.tvAlergi.visibility = View.GONE
                binding.spAlergi.visibility = View.GONE
                binding.tvChangeAlergi.visibility = View.GONE
                binding.spChangeAlergi.visibility = View.GONE
                binding.tvNutrition.visibility = View.GONE
                binding.spNutrition.visibility = View.GONE
                binding.tvChangeNutrition.visibility = View.GONE
                binding.spChangeNutrition.visibility = View.GONE
            }
            "Nutrisi" -> {
                binding.tvNutrition.visibility = View.VISIBLE
                binding.spNutrition.visibility = View.VISIBLE
                binding.tvChangeNutrition.visibility = View.VISIBLE
                binding.spChangeNutrition.visibility = View.VISIBLE
                binding.tvChange.visibility = View.VISIBLE

                binding.tvLimit.visibility = View.GONE
                binding.spLimit.visibility = View.GONE
                binding.tvPeriod.visibility = View.GONE
                binding.spPeriod.visibility = View.GONE
                binding.tvChangeLimit.visibility = View.GONE
                binding.spChangeLimit.visibility = View.GONE
                binding.tvChangePeriod.visibility = View.GONE
                binding.spChangePeriod.visibility = View.GONE
                binding.tvAlergi.visibility = View.GONE
                binding.spAlergi.visibility = View.GONE
                binding.tvChangeAlergi.visibility = View.GONE
                binding.spChangeAlergi.visibility = View.GONE
            }
            else -> {
                binding.tvAlergi.visibility = View.GONE
                binding.spAlergi.visibility = View.GONE
                binding.tvChangeAlergi.visibility = View.GONE
                binding.spChangeAlergi.visibility = View.GONE
                binding.tvLimit.visibility = View.GONE
                binding.spLimit.visibility = View.GONE
                binding.tvPeriod.visibility = View.GONE
                binding.spPeriod.visibility = View.GONE
                binding.tvChangeLimit.visibility = View.GONE
                binding.spChangeLimit.visibility = View.GONE
                binding.tvChangePeriod.visibility = View.GONE
                binding.spChangePeriod.visibility = View.GONE
                binding.tvNutrition.visibility = View.GONE
                binding.spNutrition.visibility = View.GONE
                binding.tvChangeNutrition.visibility = View.GONE
                binding.spChangeNutrition.visibility = View.GONE
                binding.tvChange.visibility = View.GONE
            }
        }
    }


    private fun handleUpdateAllergy() {
        /*val allergyId = viewModel.allergiesMap.value
            ?.filterValues { it == binding.spAlergi.selectedItem.toString() }
            ?.keys?.firstOrNull()*/
        val oldAllergy = binding.spAlergi.selectedItem?.toString()
        val newAllergy = binding.spChangeAlergi.selectedItem?.toString()

        if (!oldAllergy.isNullOrEmpty() && !newAllergy.isNullOrEmpty()) {
            viewModel.updateAllergy(selectedChild.uid, oldAllergy, newAllergy)
            Toast.makeText(this, getString(R.string.berhasil_mengubah_data), Toast.LENGTH_SHORT).show()
            intent = Intent(this, ParentActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.gagal_mengubah_data), Toast.LENGTH_SHORT).show()
            /*showErrorToast("Invalid allergy update")*/
        }
    }

    private fun handleUpdateSpending() {
        /*val spendingId = viewModel.spendingMap.value
            ?.filterValues {
                it == Pair(
                    binding.spLimit.selectedItem?.toString(),
                    binding.spPeriod.selectedItem?.toString()
                )
            }
            ?.keys?.firstOrNull()*/

        val oldLimit = binding.spLimit.selectedItem?.toString()
        val newLimit = binding.spChangeLimit.selectedItem?.toString()
        val newPeriod = binding.spChangePeriod.selectedItem?.toString()

        if (!oldLimit.isNullOrEmpty() && !newLimit.isNullOrEmpty() && !newPeriod.isNullOrEmpty()) {
            viewModel.updateSpending(selectedChild.uid, oldLimit, newLimit, newPeriod)
            Toast.makeText(this, getString(R.string.berhasil_mengubah_data), Toast.LENGTH_SHORT).show()
            intent = Intent(this, ParentActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.gagal_mengubah_data), Toast.LENGTH_SHORT).show()
            /*showErrorToast("Invalid spending update")*/
        }
    }

    private fun handleUpdateNutrition() {
        /*val nutritionId = viewModel.nutritionMap.value
            ?.filterValues {
                it == Pair(binding.spNutrition.selectedItem?.toString(), "")
            }
            ?.keys?.firstOrNull()*/

        val oldName = binding.spNutrition.selectedItem?.toString()
        val newName = binding.spChangeNutrition.selectedItem?.toString()

        if (!oldName.isNullOrEmpty() && !newName.isNullOrEmpty()) {
            viewModel.updateNutrition(selectedChild.uid, oldName, newName)
            Toast.makeText(this, getString(R.string.berhasil_mengubah_data), Toast.LENGTH_SHORT).show()
            intent = Intent(this, ParentActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.gagal_mengubah_data), Toast.LENGTH_SHORT).show()
            /*showErrorToast("Invalid nutrition update")*/
        }
    }
}
/*
class ChangeConstraintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentChangeBinding
    private val viewModel: ChangeConstraintViewModel by viewModels {
        ConstraintViewModelFactory(
            repository = ConstraintRepository(UserPreference.getInstance(this.dataStore)),
        )
    }

    private val studentViewModel: StudentViewModel by viewModels {
        ConstraintViewModelFactory(
            repository = ConstraintRepository(UserPreference.getInstance(this.dataStore)),
        )
    }

    private lateinit var selectedChild: StudentProfileModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Change Constraint"
        }

        loadParentsChildren() // Load the children into spinner
        setupObservers()
        setupListeners()

        setupSpinner(binding.spConstraint, R.array.Constraint)
        setupSpinner(binding.spChangeAlergi, R.array.Alergi)
        setupSpinner(binding.spChangeLimit, R.array.Pengeluaran)
        setupSpinner(binding.spChangePeriod, R.array.Waktu)
        setupSpinner(binding.spChangeNutrition, R.array.Nutrisi)

        viewModel.fetchAllergies()
        viewModel.fetchSpending()
        viewModel.fetchNutrition()
    }

    private fun loadParentsChildren() {
        val parentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        studentViewModel.loadParentsChilds(parentUid)

        studentViewModel.parentsChildsList.observe(this) { childs ->
            val childNames = childs.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, childNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spNamaAnak.adapter = adapter

            binding.spNamaAnak.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        selectedChild = childs[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // No action
                    }
                }
        }
    }

    private fun setupObservers() {
        viewModel.allergiesMap.observe(this) { allergiesMap ->
            if (allergiesMap.isNotEmpty()) {
                setupSpinnerData(binding.spAlergi, allergiesMap.values.toList())
            } else {
                showErrorToast("No allergy data available")
            }
        }

        viewModel.spendingMap.observe(this) { spendingMap ->
            if (spendingMap.isNotEmpty()) {
                setupSpinnerData(binding.spLimit, spendingMap.map { it.value.first })
                setupSpinnerData(binding.spPeriod, spendingMap.map { it.value.second })
            } else {
                showErrorToast("No spending data available")
            }
        }

        viewModel.nutritionMap.observe(this) { nutritionMap ->
            if (nutritionMap.isNotEmpty()) {
                setupSpinnerData(binding.spNutrition, nutritionMap.map { it.value.first })
            } else {
                showErrorToast("No nutrition data available")
            }
        }

        viewModel.updateAllergyStatus.observe(this) { statusMessage ->
            statusMessage?.let { showSuccessDialog(it) }
        }

        viewModel.updateSpendingStatus.observe(this) { statusMessage ->
            statusMessage?.let { showSuccessDialog(it) }
        }

        viewModel.updateNutritionStatus.observe(this) { statusMessage ->
            statusMessage?.let { showSuccessDialog(it) }
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let { showErrorToast(it) }
        }
    }

    private fun setupSpinner(spinner: Spinner, arrayResId: Int) {
        val items = resources.getStringArray(arrayResId)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupListeners() {
        binding.spConstraint.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedConstraint = binding.spConstraint.selectedItem.toString()
                toggleConstraintViews(selectedConstraint)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        binding.btnChange.setOnClickListener {
            if (!::selectedChild.isInitialized) {
                showErrorToast("Please select a child first.")
                return@setOnClickListener
            }

            val selectedConstraint = binding.spConstraint.selectedItem.toString()
            when (selectedConstraint) {
                "Alergi" -> handleUpdateAllergy()
                "Pengeluaran" -> handleUpdateSpending()
                "Nutrisi" -> handleUpdateNutrition()
            }
        }

        binding.infoIcon.setOnClickListener { showInfoDialog() }
    }

    private fun toggleConstraintViews(selectedConstraint: String) {
        when (selectedConstraint) {
            "Alergi" -> {
                binding.tvAlergi.visibility = View.VISIBLE
                binding.spAlergi.visibility = View.VISIBLE
                binding.tvChangeAlergi.visibility = View.VISIBLE
                binding.spChangeAlergi.visibility = View.VISIBLE
                binding.tvChange.visibility = View.VISIBLE

                binding.tvLimit.visibility = View.GONE
                binding.spLimit.visibility = View.GONE
                binding.tvPeriod.visibility = View.GONE
                binding.spPeriod.visibility = View.GONE
                binding.tvChangeLimit.visibility = View.GONE
                binding.spChangeLimit.visibility = View.GONE
                binding.tvChangePeriod.visibility = View.GONE
                binding.spChangePeriod.visibility = View.GONE
                binding.tvNutrition.visibility = View.GONE
                binding.spNutrition.visibility = View.GONE
                binding.tvChangeNutrition.visibility = View.GONE
                binding.spChangeNutrition.visibility = View.GONE
            }
            "Pengeluaran" -> {
                binding.tvLimit.visibility = View.VISIBLE
                binding.spLimit.visibility = View.VISIBLE
                binding.tvPeriod.visibility = View.VISIBLE
                binding.spPeriod.visibility = View.VISIBLE
                binding.tvChangeLimit.visibility = View.VISIBLE
                binding.spChangeLimit.visibility = View.VISIBLE
                binding.tvChangePeriod.visibility = View.VISIBLE
                binding.spChangePeriod.visibility = View.VISIBLE
                binding.tvChange.visibility = View.VISIBLE

                binding.tvAlergi.visibility = View.GONE
                binding.spAlergi.visibility = View.GONE
                binding.tvChangeAlergi.visibility = View.GONE
                binding.spChangeAlergi.visibility = View.GONE
                binding.tvNutrition.visibility = View.GONE
                binding.spNutrition.visibility = View.GONE
                binding.tvChangeNutrition.visibility = View.GONE
                binding.spChangeNutrition.visibility = View.GONE
            }
            "Nutrisi" -> {
                binding.tvNutrition.visibility = View.VISIBLE
                binding.spNutrition.visibility = View.VISIBLE
                binding.tvChangeNutrition.visibility = View.VISIBLE
                binding.spChangeNutrition.visibility = View.VISIBLE
                binding.tvChange.visibility = View.VISIBLE

                binding.tvLimit.visibility = View.GONE
                binding.spLimit.visibility = View.GONE
                binding.tvPeriod.visibility = View.GONE
                binding.spPeriod.visibility = View.GONE
                binding.tvChangeLimit.visibility = View.GONE
                binding.spChangeLimit.visibility = View.GONE
                binding.tvChangePeriod.visibility = View.GONE
                binding.spChangePeriod.visibility = View.GONE
                binding.tvAlergi.visibility = View.GONE
                binding.spAlergi.visibility = View.GONE
                binding.tvChangeAlergi.visibility = View.GONE
                binding.spChangeAlergi.visibility = View.GONE
            }
            else -> {
                binding.tvAlergi.visibility = View.GONE
                binding.spAlergi.visibility = View.GONE
                binding.tvChangeAlergi.visibility = View.GONE
                binding.spChangeAlergi.visibility = View.GONE
                binding.tvLimit.visibility = View.GONE
                binding.spLimit.visibility = View.GONE
                binding.tvPeriod.visibility = View.GONE
                binding.spPeriod.visibility = View.GONE
                binding.tvChangeLimit.visibility = View.GONE
                binding.spChangeLimit.visibility = View.GONE
                binding.tvChangePeriod.visibility = View.GONE
                binding.spChangePeriod.visibility = View.GONE
                binding.tvNutrition.visibility = View.GONE
                binding.spNutrition.visibility = View.GONE
                binding.tvChangeNutrition.visibility = View.GONE
                binding.spChangeNutrition.visibility = View.GONE
                binding.tvChange.visibility = View.GONE
            }
        }
    }


    private fun handleUpdateAllergy() {
        val allergyId = viewModel.allergiesMap.value
            ?.filterValues { it == binding.spAlergi.selectedItem.toString() }
            ?.keys?.firstOrNull()

        val newAllergy = binding.spChangeAlergi.selectedItem?.toString()

        if (!allergyId.isNullOrEmpty() && !newAllergy.isNullOrEmpty()) {
            viewModel.updateAllergy(selectedChild.uid, allergyId, newAllergy)
        } else {
            showErrorToast("Invalid allergy update")
        }
    }

    private fun handleUpdateSpending() {
        val spendingId = viewModel.spendingMap.value
            ?.filterValues {
                it == Pair(
                    binding.spLimit.selectedItem?.toString(),
                    binding.spPeriod.selectedItem?.toString()
                )
            }
            ?.keys?.firstOrNull()

        val newLimit = binding.spChangeLimit.selectedItem?.toString()
        val newPeriod = binding.spChangePeriod.selectedItem?.toString()

        if (!spendingId.isNullOrEmpty() && !newLimit.isNullOrEmpty() && !newPeriod.isNullOrEmpty()) {
            viewModel.updateSpending(selectedChild.uid, spendingId, newLimit, newPeriod)
        } else {
            showErrorToast("Invalid spending update")
        }
    }

    private fun handleUpdateNutrition() {
        val nutritionId = viewModel.nutritionMap.value
            ?.filterValues {
                it == Pair(binding.spNutrition.selectedItem?.toString(), "")
            }
            ?.keys?.firstOrNull()

        val newName = binding.spChangeNutrition.selectedItem?.toString()

        if (!nutritionId.isNullOrEmpty() && !newName.isNullOrEmpty()) {
            viewModel.updateNutrition(selectedChild.uid, nutritionId, newName)
        } else {
            showErrorToast("Invalid nutrition update")
        }
    }

    private fun setupSpinnerData(spinner: Spinner, items: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun showSuccessDialog(message: String) {
        if (!isFinishing && !isDestroyed) {
            AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showInfoDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Constraint Information")
            .setMessage("The parent chooses the child and the constraint to change.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}*/
