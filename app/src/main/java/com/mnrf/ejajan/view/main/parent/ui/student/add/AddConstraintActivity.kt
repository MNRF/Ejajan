package com.mnrf.ejajan.view.main.parent.ui.student.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.ActivityParentAddBinding
import com.mnrf.ejajan.databinding.ActivityParentBinding

class AddConstraintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParentAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val allergy = resources.getStringArray(R.array.Allergy)

        if (binding.spConstraint != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, allergy)
            binding.spConstraint.adapter = adapter

            binding.spConstraint.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Toast.makeText(this@AddConstraintActivity,
                        /*getString(R.string.selected_item) + " " +
                                "" + */allergy[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        val btnTambah = findViewById<Button>(R.id.btn_create)

        btnTambah.setOnClickListener {
            val intent = Intent(this, AddConstraintActivity::class.java)
            startActivity(intent)
        }
    }
}