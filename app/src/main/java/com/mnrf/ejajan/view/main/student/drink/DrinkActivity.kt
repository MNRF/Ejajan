package com.mnrf.ejajan.view.main.student.drink

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnrf.ejajan.databinding.ActivityStudentDrinkBinding
import com.mnrf.ejajan.view.main.student.adapter.SeeAllListAdapter
import com.mnrf.ejajan.view.utils.ViewModelFactory

class DrinkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDrinkBinding
    private val drinkViewModel: DrinkViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: SeeAllListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDrinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Drink"
        }

        adapter = SeeAllListAdapter()
        binding.rvStudentDrink.layoutManager = LinearLayoutManager(this)
        binding.rvStudentDrink.adapter = adapter

        drinkViewModel.menuList.observe(this) { menuList ->
            adapter.submitList(menuList)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}