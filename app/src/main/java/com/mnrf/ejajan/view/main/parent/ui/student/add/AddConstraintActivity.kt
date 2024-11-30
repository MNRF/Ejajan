package com.mnrf.ejajan.view.main.parent.ui.student.add

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.data.repository.ConstraintRepository
import com.mnrf.ejajan.databinding.ActivityParentAddBinding
import com.mnrf.ejajan.view.utils.ViewModelFactory

class AddConstraintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentAddBinding
    private lateinit var viewModel: AddConstraintViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val repository = ConstraintRepository()
        viewModel = ViewModelProvider(this, ConstraintViewModelFactory(repository))[AddConstraintViewModel::class.java]

        setupSpinner(binding.spConstraint, R.array.Constraint)
        setupSpinner(binding.spAlergi, R.array.Allergy)
        setupSpinner(binding.spLimit, R.array.Spending)
        setupSpinner(binding.spPeriod, R.array.Time)

        binding.btnCreate.setOnClickListener {
            val allergyName = binding.spAlergi.selectedItem.toString()
            if (allergyName.isNotEmpty()) {
                val allergy = AllergyModel(name = allergyName)
                viewModel.addAllergy(allergy) // Use the ViewModel function here
                Toast.makeText(this, "Allergy added successfully", Toast.LENGTH_SHORT).show()
                finish() // Go back after adding
            } else {
                Toast.makeText(this, "Pilih alergi terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }


        binding.infoIcon.setOnClickListener {
            showInfoDialog()
        }
    }

    private fun setupSpinner(spinner: Spinner, arrayResId: Int) {
        val items = resources.getStringArray(arrayResId)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Use dropdown style for spinner
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                handleSpinnerSelection()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optionally handle "nothing selected" case here
            }
        }
    }

    private fun handleSpinnerSelection() {
        when (binding.spConstraint.selectedItem.toString()) {
            "Allergy" -> {
                // Show only allergy section
                binding.tvAlergi.visibility = View.VISIBLE
                binding.spAlergi.visibility = View.VISIBLE
                binding.spLimit.visibility = View.GONE
                binding.spPeriod.visibility = View.GONE
                binding.tvLimit.visibility = View.GONE
                binding.tvPeriod.visibility = View.GONE
            }
            "Spending" -> {
                // Show spending and time period section
                binding.spLimit.visibility = View.VISIBLE
                binding.spPeriod.visibility = View.VISIBLE
                binding.tvLimit.visibility = View.VISIBLE
                binding.tvPeriod.visibility = View.VISIBLE
                binding.spAlergi.visibility = View.GONE
                binding.tvAlergi.visibility = View.GONE
            }
            else -> {
                // Hide all sections if no valid option is selected
                binding.spAlergi.visibility = View.GONE
                binding.spLimit.visibility = View.GONE
                binding.spPeriod.visibility = View.GONE
                binding.tvLimit.visibility = View.GONE
                binding.tvPeriod.visibility = View.GONE
            }
        }
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
