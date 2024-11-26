package com.mnrf.ejajan.view.main.student

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mnrf.ejajan.R
import com.mnrf.ejajan.view.main.student.drink.DrinkActivity
import com.mnrf.ejajan.view.main.student.food.FoodActivity
import com.mnrf.ejajan.view.main.student.healty.HealtyChoicesActivity
import com.mnrf.ejajan.view.main.student.menu.MenuStudentActivity
import com.mnrf.ejajan.view.main.student.personal.PersonalPicksActivity
import com.mnrf.ejajan.view.main.student.special.SpecialOffersActivity

class StudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        findViewById<View>(R.id.seeall_personal).setOnClickListener {
            startActivity(Intent(this, PersonalPicksActivity::class.java))
        }

        findViewById<View>(R.id.seeall_healty).setOnClickListener {
            startActivity(Intent(this, HealtyChoicesActivity::class.java))
        }

        findViewById<View>(R.id.seeall_special).setOnClickListener {
            startActivity(Intent(this, SpecialOffersActivity::class.java))
        }

        findViewById<View>(R.id.seeall_food).setOnClickListener {
            startActivity(Intent(this, FoodActivity::class.java))
        }

        findViewById<View>(R.id.seeall_drink).setOnClickListener {
            startActivity(Intent(this, DrinkActivity::class.java))
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, MenuStudentActivity::class.java)
            startActivity(intent)
        }
    }
}