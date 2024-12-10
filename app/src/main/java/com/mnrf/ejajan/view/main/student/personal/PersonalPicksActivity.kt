package com.mnrf.ejajan.view.main.student.personal

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.ActivityStudentPersonalPicksBinding

class PersonalPicksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentPersonalPicksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentPersonalPicksBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}