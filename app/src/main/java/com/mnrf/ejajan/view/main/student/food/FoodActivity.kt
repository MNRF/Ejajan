package com.mnrf.ejajan.view.main.student.food

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnrf.ejajan.databinding.ActivityStudentFoodBinding
import com.mnrf.ejajan.view.main.student.adapter.SeeAllListAdapter
import com.mnrf.ejajan.view.utils.ViewModelFactory

class FoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentFoodBinding
    private val foodViewModel: FoodViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: SeeAllListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Food"
        }

        adapter = SeeAllListAdapter()
        binding.rvStudentFood.layoutManager = LinearLayoutManager(this)
        binding.rvStudentFood.adapter = adapter

        foodViewModel.menuList.observe(this) { menuList ->
            adapter.submitList(menuList)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}