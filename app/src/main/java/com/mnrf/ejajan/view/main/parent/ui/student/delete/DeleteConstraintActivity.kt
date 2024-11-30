package com.mnrf.ejajan.view.main.parent.ui.student.delete

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.repository.ConstraintRepository
import com.mnrf.ejajan.databinding.ActivityParentDeleteBinding
import com.mnrf.ejajan.view.main.parent.ui.student.ConstraintViewModelFactory

class DeleteConstraintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentDeleteBinding
    private val repository = ConstraintRepository()
    private val viewModel: DeleteConstraintViewModel by viewModels {
        ConstraintViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Delete Constraint"
        }

        setupObservers()
        setupListeners()

        // Fetch data
        viewModel.fetchAllergies()
        viewModel.fetchSpending()

        setupSpinner(binding.spConstraint, R.array.Constraint)
    }

    private fun setupObservers() {
        // Observe allergies
        viewModel.allergiesMap.observe(this) { allergiesMap ->
            if (allergiesMap.isNotEmpty()) {
                setupSpinnerData(binding.spAlergi, allergiesMap.values.toList())
            } else {
                showErrorToast("No allergy data available")
            }
        }

        // Observe spending
        viewModel.spendingMap.observe(this) { spendingMap ->
            if (spendingMap.isNotEmpty()) {
                setupSpinnerData(binding.spLimit, spendingMap.map { it.value.first })
                setupSpinnerData(binding.spPeriod, spendingMap.map { it.value.second })
            } else {
                showErrorToast("No spending data available")
            }
        }

        // Observe deletion success or error
        viewModel.deleteSuccess.observe(this) { success ->
            if (success) showSuccessDialog("Deleted successfully")
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let { showErrorToast(it) }
        }
    }

    private fun setupListeners() {
        binding.spConstraint.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedConstraint = binding.spConstraint.selectedItem.toString()
                when (selectedConstraint) {
                    "Allergy" -> {
                        binding.tvAlergi.visibility = View.VISIBLE
                        binding.spAlergi.visibility = View.VISIBLE
                        binding.tvLimit.visibility = View.GONE
                        binding.spLimit.visibility = View.GONE
                        binding.tvPeriod.visibility = View.GONE
                        binding.spPeriod.visibility = View.GONE
                    }
                    "Spending" -> {
                        binding.tvLimit.visibility = View.VISIBLE
                        binding.spLimit.visibility = View.VISIBLE
                        binding.tvPeriod.visibility = View.VISIBLE
                        binding.spPeriod.visibility = View.VISIBLE
                        binding.tvAlergi.visibility = View.GONE
                        binding.spAlergi.visibility = View.GONE
                    }
                    else -> {
                        binding.tvAlergi.visibility = View.GONE
                        binding.spAlergi.visibility = View.GONE
                        binding.tvLimit.visibility = View.GONE
                        binding.spLimit.visibility = View.GONE
                        binding.tvPeriod.visibility = View.GONE
                        binding.spPeriod.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        binding.btnDelete.setOnClickListener {
            val constraintType = binding.spConstraint.selectedItem?.toString()
            when (constraintType) {
                "Allergy" -> handleDeleteAllergy()
                "Spending" -> handleDeleteSpending()
                else -> showErrorToast("Invalid constraint selection")
            }
        }

        binding.infoIcon.setOnClickListener { showInfoDialog() }
    }

    private fun handleDeleteAllergy() {
        val allergyName = binding.spAlergi.selectedItem?.toString()
        if (!allergyName.isNullOrEmpty()) {
            val allergyId = viewModel.allergiesMap.value?.filterValues { it == allergyName }?.keys?.firstOrNull()
            if (allergyId != null) {
                viewModel.deleteAllergy(allergyId)
            } else {
                showErrorToast("Invalid allergy selection")
            }
        } else {
            showErrorToast("Please select an allergy to delete")
        }
    }

    private fun handleDeleteSpending() {
        val amount = binding.spLimit.selectedItem?.toString()
        val period = binding.spPeriod.selectedItem?.toString()
        if (!amount.isNullOrEmpty() && !period.isNullOrEmpty()) {
            val spendingId = viewModel.spendingMap.value?.filterValues { it == Pair(amount, period) }?.keys?.firstOrNull()
            if (spendingId != null) {
                viewModel.deleteSpending(spendingId)
            } else {
                showErrorToast("Invalid spending selection")
            }
        } else {
            showErrorToast("Please select spending details to delete")
        }
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
        AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun showInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle("Constraint Information")
            .setMessage("The parent chooses the constraint between these two. If the parent chooses allergy, then the parent only sees the allergy section, while if the parent chooses spending, then the nominal limit and the time period for spending are displayed.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
