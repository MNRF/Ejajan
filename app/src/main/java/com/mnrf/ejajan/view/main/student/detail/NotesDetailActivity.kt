package com.mnrf.ejajan.view.main.student.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mnrf.ejajan.databinding.ActivityNotesDetailBinding
import com.mnrf.ejajan.view.main.student.adapter.NotesAdapter
import com.mnrf.ejajan.view.utils.ViewModelFactory

class NotesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotesDetailBinding

    private val notesDetailViewModel: NotesDetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Notes"
        }

        adapter = NotesAdapter()
        binding.rvNotesDetail.layoutManager = GridLayoutManager(this, 3)
        binding.rvNotesDetail.adapter = adapter

        notesDetailViewModel.menuList.observe(this) { menuList ->
            adapter.submitList(menuList)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}