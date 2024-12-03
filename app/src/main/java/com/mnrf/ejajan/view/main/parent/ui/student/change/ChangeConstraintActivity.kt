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
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.pref.UserPreference
import com.mnrf.ejajan.data.pref.dataStore
import com.mnrf.ejajan.data.repository.ConstraintRepository
import com.mnrf.ejajan.databinding.ActivityParentChangeBinding
import com.mnrf.ejajan.view.main.parent.ui.student.ConstraintViewModelFactory

class ChangeConstraintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentChangeBinding
    private val viewModel: ChangeConstraintViewModel by viewModels {
        ConstraintViewModelFactory(
            repository = ConstraintRepository(UserPreference.getInstance(this.dataStore)),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Change Constraint"
        }

        setupObservers()
        setupListeners()

        // Setting up spinners ini nanti diganti dengan data yang diambil firebase firestore
        setupSpinner(binding.spConstraint, R.array.Constraint)
//        setupSpinner(binding.spAlergi, R.array.Allergy)
//        setupSpinner(binding.spLimit, R.array.Spending)
//        setupSpinner(binding.spPeriod, R.array.Time)

        // ini buat spinner data baru full item string array
        setupSpinner(binding.spChangeAlergi, R.array.Allergy)
        setupSpinner(binding.spChangeLimit, R.array.Spending)
        setupSpinner(binding.spChangePeriod, R.array.Time)

        viewModel.fetchAllergies()
        viewModel.fetchSpending()

//        binding.btnChange.setOnClickListener {
//            val intent = Intent(this, ChangeConstraintActivity::class.java)
//            startActivity(intent)
//        }

//        binding.infoIcon.setOnClickListener {
//            showInfoDialog()
//        }
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

        viewModel.updateAllergyStatus.observe(this) { statusMessage ->
            statusMessage?.let { showSuccessDialog(it) }
        }

        // Observe deletion success or error
        viewModel.updateSpendingStatus.observe(this) { statusMessage ->
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
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedConstraint = binding.spConstraint.selectedItem.toString()
                when (selectedConstraint) {
                    "Allergy" -> {
                        // Tampilkan komponen yang terkait dengan alergi
                        binding.tvAlergi.visibility = View.VISIBLE
                        binding.spAlergi.visibility = View.VISIBLE
                        binding.tvChangeAlergi.visibility = View.VISIBLE
                        binding.spChangeAlergi.visibility = View.VISIBLE
                        binding.tvLimit.visibility = View.GONE
                        binding.spLimit.visibility = View.GONE
                        binding.tvPeriod.visibility = View.GONE
                        binding.spPeriod.visibility = View.GONE
                    }
                    "Spending" -> {
                        // Tampilkan komponen yang terkait dengan spending
                        binding.tvLimit.visibility = View.VISIBLE
                        binding.spLimit.visibility = View.VISIBLE
                        binding.tvPeriod.visibility = View.VISIBLE
                        binding.spPeriod.visibility = View.VISIBLE
                        binding.spChangeLimit.visibility = View.VISIBLE
                        binding.spChangePeriod.visibility = View.VISIBLE
                        binding.tvAlergi.visibility = View.GONE
                        binding.spAlergi.visibility = View.GONE
                        binding.tvChangeAlergi.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        binding.btnChange.setOnClickListener {
            val selectedConstraint = binding.spConstraint.selectedItem.toString()
            when (selectedConstraint) {
                "Allergy" -> handleUpdateAllergy()
                "Spending" -> handleUpdateSpending()
            }
        }
        binding.infoIcon.setOnClickListener { showInfoDialog() }
    }

    private fun handleUpdateAllergy() {
        val allergyId = viewModel.allergiesMap.value
            ?.filterValues { it == binding.spAlergi.selectedItem.toString() }
            ?.keys?.firstOrNull()

        val newAllergy = binding.spChangeAlergi.selectedItem?.toString()

        if (!allergyId.isNullOrEmpty() && !newAllergy.isNullOrEmpty()) {
            viewModel.updateAllergy(allergyId, newAllergy)
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
            viewModel.updateSpending(spendingId, newLimit, newPeriod)
        } else {
            showErrorToast("Invalid spending update")
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
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
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
            .setMessage("The parent chooses the constraint between these two. If the parent chooses allergy, then the parent only sees the allergy section, while if the parent chooses spending, then the nominal limit and the time period for spending are displayed.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}