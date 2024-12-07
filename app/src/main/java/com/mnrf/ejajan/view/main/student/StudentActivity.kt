package com.mnrf.ejajan.view.main.student

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.ActivityStudentBinding
import com.mnrf.ejajan.view.login.LoginStudent
import com.mnrf.ejajan.view.main.student.adapter.MenuHomeStudentListAdapter
import com.mnrf.ejajan.view.main.student.adapter.SeeAllListAdapter
import com.mnrf.ejajan.view.main.student.adapter.SpecialAdapter
import com.mnrf.ejajan.view.main.student.cart.CartActivity
import com.mnrf.ejajan.view.main.student.drink.DrinkActivity
import com.mnrf.ejajan.view.main.student.food.FoodActivity
import com.mnrf.ejajan.view.main.student.healty.HealtyChoicesActivity
import com.mnrf.ejajan.view.main.student.menu.MenuStudentActivity
import com.mnrf.ejajan.view.main.student.personal.PersonalPicksActivity
import com.mnrf.ejajan.view.main.student.special.SpecialOffersActivity
import com.mnrf.ejajan.view.slider.ImageSliderAdapter
import com.mnrf.ejajan.view.slider.ImageSliderData
import com.mnrf.ejajan.view.utils.ViewModelFactory

class StudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private val listImage = ArrayList<ImageSliderData>()
    private val circleImage = ArrayList<TextView>()

    private val sliderHandler = Handler(Looper.getMainLooper())
    private val sliderRunnable = Runnable {
        binding.vpStudentLogo.currentItem =
            (binding.vpStudentLogo.currentItem + 1) % listImage.size
    }

    private val studentViewModel: StudentViewModel by viewModels {
        ViewModelFactory.getInstance(this@StudentActivity)
    }

    private lateinit var foodAdapter: MenuHomeStudentListAdapter
    private lateinit var drinkAdapter: MenuHomeStudentListAdapter
    private lateinit var healtAdapter: MenuHomeStudentListAdapter
    private lateinit var specialAdapter: SpecialAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_student, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.cart -> startActivity(Intent(this, CartActivity::class.java))
            R.id.logout -> showLogoutConfirmationDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        setupListeners()
        setupImageSlider()

        onBackPressedDispatcher.addCallback(this) {
            showExitConfirmationDialog()
        }

        foodAdapter = MenuHomeStudentListAdapter()
        drinkAdapter = MenuHomeStudentListAdapter()
        healtAdapter = MenuHomeStudentListAdapter()
        specialAdapter = SpecialAdapter()

        binding.rvFood.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvFood.adapter = foodAdapter

        binding.rvDrink.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDrink.adapter = drinkAdapter

        binding.rvHealty.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHealty.adapter = healtAdapter

        binding.rvSpecial.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSpecial.adapter = specialAdapter

        studentViewModel.foodMenuList.observe(this) { menuList ->
            foodAdapter.submitList(menuList)
        }

        studentViewModel.drinkMenuList.observe(this) { menuList ->
            drinkAdapter.submitList(menuList)
        }

        studentViewModel.healtMenuList.observe(this) { menuList ->
            healtAdapter.submitList(menuList)
        }

        studentViewModel.specialMenuList.observe(this) { menuList ->
            specialAdapter.submitList(menuList)
        }
    }

    private fun setupListeners() {
        with(binding) {
            seeallPersonal.setOnClickListener {
                startActivity(Intent(this@StudentActivity, PersonalPicksActivity::class.java))
            }

            seeallHealty.setOnClickListener {
                startActivity(Intent(this@StudentActivity, HealtyChoicesActivity::class.java))
            }

            seeallSpecial.setOnClickListener {
                startActivity(Intent(this@StudentActivity, SpecialOffersActivity::class.java))
            }

            seeallFood.setOnClickListener {
                startActivity(Intent(this@StudentActivity, FoodActivity::class.java))
            }

            seeallDrink.setOnClickListener {
                startActivity(Intent(this@StudentActivity, DrinkActivity::class.java))
            }

            fab.setOnClickListener {
                startActivity(Intent(this@StudentActivity, CartActivity::class.java))
            }
        }
    }


    private fun setupImageSlider() {
        listImage.apply {
            add(ImageSliderData(R.drawable.image1_slider_student))
            add(ImageSliderData(R.drawable.image2_slider_student))
            add(ImageSliderData(R.drawable.image3_slider_student))
        }

        // Inisialisasi adapter dan set ke ViewPager2
        imageSliderAdapter = ImageSliderAdapter(listImage)
        binding.vpStudentLogo.adapter = imageSliderAdapter

        setupCircleIndicator()

        binding.vpStudentLogo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupSelectedIndicator(position)

                // Restart auto-slide timer
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })

        // Mulai auto-slide
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    private fun setupCircleIndicator() {
        binding.llCircle.removeAllViews()
        for (i in 0 until listImage.size) {
            val indicator = TextView(this).apply {
                text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
                textSize = 18f
                setTextColor(ContextCompat.getColor(this@StudentActivity, R.color.white))
            }
            circleImage.add(indicator)
            binding.llCircle.addView(indicator)
        }
    }

    private fun setupSelectedIndicator(position: Int) {
        circleImage.forEachIndexed { index, textView ->
            textView.setTextColor(
                if (index == position)
                    ContextCompat.getColor(this, R.color.blue1)
                else
                    ContextCompat.getColor(this, R.color.white)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.logout)
            .setMessage(R.string.logout_confirmation)
            .setPositiveButton(R.string.ya) { _, _ ->
                auth.signOut()
                studentViewModel.logout()
                val intent = Intent(this, LoginStudent::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .setNegativeButton(R.string.tidak) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.exit)
            .setMessage(R.string.logout_confirmation)
            .setPositiveButton(R.string.ya) { _, _ ->
                finish()
            }
            .setNegativeButton(R.string.tidak) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
