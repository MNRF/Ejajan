package com.mnrf.ejajan.view.main.parent.ui.student.add

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.data.model.NutritionModel
import com.mnrf.ejajan.data.model.SpendingModel
import com.mnrf.ejajan.data.model.StudentProfileModel
import com.mnrf.ejajan.data.pref.UserPreference
import com.mnrf.ejajan.data.pref.dataStore
import com.mnrf.ejajan.data.repository.ConstraintRepository
import com.mnrf.ejajan.databinding.ActivityParentAddBinding
import com.mnrf.ejajan.view.main.merchant.ui.menu.add.MerchantAddMenuViewModel
import com.mnrf.ejajan.view.main.parent.ui.student.ConstraintViewModelFactory
import com.mnrf.ejajan.view.main.parent.ui.student.StudentViewModel
import com.mnrf.ejajan.view.utils.ViewModelFactory

class AddConstraintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentAddBinding
    private val viewModel: AddConstraintViewModel by viewModels {
        ConstraintViewModelFactory(
            repository = ConstraintRepository(UserPreference.getInstance(this.dataStore)),
        )
    }

    private val studentviewModel: StudentViewModel by viewModels {
        ConstraintViewModelFactory(
            repository = ConstraintRepository(UserPreference.getInstance(this.dataStore)),
        )
    }

    private var isSpendingAdded = false
    private lateinit var selectedChild: StudentProfileModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Add Constraint"
        }

        setupSpinner(binding.spConstraint, R.array.Constraint)
        setupSpinner(binding.spAlergi, R.array.Alergi)
        setupSpinner(binding.spLimit, R.array.Pengeluaran)
        setupSpinner(binding.spPeriod, R.array.Waktu)
        setupSpinner(binding.spNutrition, R.array.Nutrisi)
        /*setupSpinner(binding.spMineral, R.array.Mineral)*/

        checkIfSpendingAdded()

        loadParentsChilds()

        binding.btnCreate.setOnClickListener {
            if (!::selectedChild.isInitialized) {
                Toast.makeText(this, "Pilih nama anak terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (binding.spConstraint.selectedItem.toString()) {
                "Alergi" -> {
                    val allergyName = binding.spAlergi.selectedItem.toString()
                    if (allergyName.isNotEmpty()) {
                        val allergy = AllergyModel(name = allergyName, student_uid = selectedChild.uid)
                        viewModel.addAllergy(
                            allergy,
                            onSuccess = { showSuccessDialog("Allergy added successfully") },
                            onFailure = { e -> Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
                        )
                    } else {
                        Toast.makeText(this, "Pilih alergi terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                }
                "Pengeluaran" -> {
                    val spendingCategory = binding.spLimit.selectedItem.toString()
                    val period = binding.spPeriod.selectedItem.toString()
                    if (spendingCategory.isNotEmpty() && period.isNotEmpty() && !isSpendingAdded) {
                        val spending = SpendingModel(amount = spendingCategory, period = period, student_uid = selectedChild.uid)
                        viewModel.addSpending(
                            spending,
                            onSuccess = {
                                isSpendingAdded = true
                                showSuccessDialog("Spending added successfully")
                                checkIfSpendingAdded()
                            },
                            onFailure = { e -> Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
                        )
                    } else {
                        if (isSpendingAdded) {
                            Toast.makeText(this, "Spending sudah ditambahkan sebelumnya", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Pilih spending terlebih dahulu", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                "Nutrisi" -> {
                    val nutritionName = binding.spNutrition.selectedItem.toString()
                    if (nutritionName.isNotEmpty()) {
                        val nutrition = NutritionModel(name = nutritionName, student_uid = selectedChild.uid)
                        viewModel.addNutrition(
                            nutrition,
                            onSuccess = { showSuccessDialog("Nutrisi berhasil ditambahkan") },
                            onFailure = { e -> Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
                        )
                    } else {
                        Toast.makeText(this, "Pilih jenis nutrisi terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> Toast.makeText(this, "Pilih constraint terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }


        binding.infoIcon.setOnClickListener {
            showInfoDialog()
        }
    }

    private fun loadParentsChilds() {
        val parentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        studentviewModel.loadParentsChilds(parentUid)

        studentviewModel.parentsChildsList.observe(this) { childs ->
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

    private fun setupSpinner(spinner: Spinner, arrayResId: Int) {
        val items = resources.getStringArray(arrayResId)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                handleSpinnerSelection()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle "nothing selected" case here
            }
        }
    }

    private fun handleSpinnerSelection() {
        when (binding.spConstraint.selectedItem.toString()) {
            "Alergi" -> {
                binding.tvAlergi.visibility = View.VISIBLE
                binding.spAlergi.visibility = View.VISIBLE
                binding.spLimit.visibility = View.GONE
                binding.spPeriod.visibility = View.GONE
                binding.tvLimit.visibility = View.GONE
                binding.tvPeriod.visibility = View.GONE
            }
            "Pengeluaran" -> {
                if (!isSpendingAdded) {
                    binding.spLimit.visibility = View.VISIBLE
                    binding.spPeriod.visibility = View.VISIBLE
                    binding.tvLimit.visibility = View.VISIBLE
                    binding.tvPeriod.visibility = View.VISIBLE
                }
                binding.spAlergi.visibility = View.GONE
                binding.tvAlergi.visibility = View.GONE
            }
            "Nutrisi" -> {
                binding.spNutrition.visibility = View.VISIBLE
                binding.tvNutrition.visibility = View.VISIBLE
                /*binding.tvMineral.visibility = View.VISIBLE
                binding.spMineral.visibility = View.VISIBLE*/

                binding.spAlergi.visibility = View.GONE
                binding.spLimit.visibility = View.GONE
                binding.spPeriod.visibility = View.GONE
                binding.tvAlergi.visibility = View.GONE
                binding.tvLimit.visibility = View.GONE
                binding.tvPeriod.visibility = View.GONE
            }
            else -> {
                binding.spAlergi.visibility = View.GONE
                binding.spLimit.visibility = View.GONE
                binding.spPeriod.visibility = View.GONE
                binding.tvLimit.visibility = View.GONE
                binding.tvPeriod.visibility = View.GONE
            }
        }
    }

    private fun checkIfSpendingAdded() {
        if (isSpendingAdded) {
            disableSpendingSpinner()
        }
    }

    private fun disableSpendingSpinner() {
        binding.spLimit.isEnabled = false
        binding.spPeriod.isEnabled = false
        binding.spLimit.visibility = View.GONE
        binding.spPeriod.visibility = View.GONE
        binding.tvLimit.visibility = View.GONE
        binding.tvPeriod.visibility = View.GONE
    }

    private fun showSuccessDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Berhasil")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun showInfoDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Informasi Batasan")
            .setMessage("The parent chooses the constraint between these two. If the parent chooses allergy, then the parent only sees the allergy section, while if the parent chooses spending, then the nominal limit and the time period for spending are displayed.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

