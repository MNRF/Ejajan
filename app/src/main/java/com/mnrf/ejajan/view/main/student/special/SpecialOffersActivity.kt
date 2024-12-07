package com.mnrf.ejajan.view.main.student.special

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.ActivityStudentSpecialOffersBinding
import com.mnrf.ejajan.view.main.student.adapter.SeeAllListAdapter
import com.mnrf.ejajan.view.main.student.adapter.SpecialAdapter
import com.mnrf.ejajan.view.main.student.adapter.SpecialListAdapter
import com.mnrf.ejajan.view.utils.ViewModelFactory

class SpecialOffersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentSpecialOffersBinding
    private val specialOffersViewModel: SpecialOffersViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: SpecialListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentSpecialOffersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Special Offers"
        }

        adapter = SpecialListAdapter()
        binding.rvSpecial.layoutManager = LinearLayoutManager(this)
        binding.rvSpecial.adapter = adapter

        specialOffersViewModel.menuList.observe(this) { menuList ->
            adapter.submitList(menuList)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}