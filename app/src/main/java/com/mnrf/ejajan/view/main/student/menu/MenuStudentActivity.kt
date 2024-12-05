package com.mnrf.ejajan.view.main.student.menu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.ActivityStudentBinding
import com.mnrf.ejajan.databinding.ActivityStudentMenuBinding
import com.mnrf.ejajan.view.main.merchant.ui.menu.MenuListAdapter
import com.mnrf.ejajan.view.main.student.StudentViewModel
import com.mnrf.ejajan.view.main.student.adapter.MenuStudentListAdapter
import com.mnrf.ejajan.view.utils.ViewModelFactory

class MenuStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentMenuBinding
    private val menuStudentViewModel: MenuStudentViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: MenuStudentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Menu"
        }

        adapter = MenuStudentListAdapter()
        binding.rvMenuStudent.layoutManager = LinearLayoutManager(this)
        binding.rvMenuStudent.adapter = adapter

        menuStudentViewModel.menuList.observe(this) { menuList ->
            adapter.submitList(menuList)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}