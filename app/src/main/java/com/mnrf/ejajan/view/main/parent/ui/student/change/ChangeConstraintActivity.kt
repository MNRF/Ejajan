package com.mnrf.ejajan.view.main.parent.ui.student.change

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.ActivityParentChangeBinding

class ChangeConstraintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Setting up spinners
        setupSpinner(binding.spConstraint, R.array.Constraint)
        setupSpinner(binding.spAlergi, R.array.Allergy)
        setupSpinner(binding.spLimit, R.array.Spending)
        setupSpinner(binding.spPeriod, R.array.Time)

        binding.btnChange.setOnClickListener {
            val intent = Intent(this, ChangeConstraintActivity::class.java)
            startActivity(intent)
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