package com.mnrf.ejajan.view.main.parent

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.pref.ThemePreferences
import com.mnrf.ejajan.data.pref.themeDataStore
import com.mnrf.ejajan.databinding.ActivityParentBinding
import com.mnrf.ejajan.view.main.parent.ui.topup.ConfirmationActivity.Companion.TRANSACTION_SUCCESS
import com.mnrf.ejajan.view.main.parent.ui.topup.TopUpViewModel
import com.mnrf.ejajan.view.utils.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ParentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParentBinding
    private lateinit var pref: ThemePreferences
    private val topUpViewModel: TopUpViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_parent) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home2, R.id.navigation_report, R.id.navigation_student, R.id.navigation_setting2
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        pref = ThemePreferences.getInstance(this.themeDataStore)
        applyThemeSettings()

        val topUpAmount = intent.getStringExtra(TRANSACTION_SUCCESS)
        if (topUpAmount != null) {
            topUpViewModel.topUp(topUpAmount.toInt())
        }
    }

    private fun applyThemeSettings() {
        // Use lifecycleScope to fetch theme setting
        lifecycleScope.launch {
            val isDarkModeActive = pref.getThemeSetting().first()
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_parent)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object {
        const val TRANSACTION_SUCCESS = "transaction_success"
        const val TRANSACTION_FAILED = "transaction_failed"
    }
}