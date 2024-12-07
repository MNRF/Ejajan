package com.mnrf.ejajan.view.main.student.healty

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnrf.ejajan.databinding.ActivityStudentHealtyChoicesBinding
import com.mnrf.ejajan.view.main.student.adapter.SeeAllListAdapter
import com.mnrf.ejajan.view.utils.ViewModelFactory

class HealtyChoicesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentHealtyChoicesBinding
    private val healtyViewModel: HealtyViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: SeeAllListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentHealtyChoicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Healty Choices"
        }

        adapter = SeeAllListAdapter()
        binding.rvHealtyChoices.layoutManager = LinearLayoutManager(this)
        binding.rvHealtyChoices.adapter = adapter

        healtyViewModel.menuList.observe(this) { menuList ->
            adapter.submitList(menuList)
        }

        healtyViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        binding.btnAllProduct.setOnClickListener {
            healtyViewModel.fetchMenuList("isHealthy")
        }

        binding.btnLowCalories.setOnClickListener {
            healtyViewModel.fetchMenuLowList("isLowCalories")
        }

        binding.btnHighProtein.setOnClickListener {
            healtyViewModel.fetchMenuHighList("isHighProtein")
        }

        binding.btnVegetarian.setOnClickListener {
            healtyViewModel.fetchMenuVegetariantList("isVegetarian")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}