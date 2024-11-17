package com.mnrf.ejajan.view.onboarding

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.mnrf.ejajan.R
import com.mnrf.ejajan.view.onboarding.merchant.MerchantOnboarding1
import com.mnrf.ejajan.view.onboarding.merchant.MerchantOnboarding2
import com.mnrf.ejajan.view.onboarding.merchant.MerchantOnboarding3
import com.mnrf.ejajan.view.onboarding.parent.ParentOnboarding1
import com.mnrf.ejajan.view.onboarding.parent.ParentOnboarding2
import com.mnrf.ejajan.view.onboarding.parent.ParentOnboarding3
import com.mnrf.ejajan.view.onboarding.student.StudentOnboarding1
import com.mnrf.ejajan.view.onboarding.student.StudentOnboarding2
import com.mnrf.ejajan.view.onboarding.student.StudentOnboarding3

class OnboardingViewPager : FragmentActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_view_pager)

        val role = intent.getStringExtra("ROLE_KEY")

        viewPager = findViewById(R.id.viewPager)

        val fragments = when (role) {
            "parent" -> arrayListOf(
                ParentOnboarding1(),
                ParentOnboarding2(),
                ParentOnboarding3(),
                StartToLogin.newInstance("parent")
            )
            "student" -> arrayListOf(
                StudentOnboarding1(),
                StudentOnboarding2(),
                StudentOnboarding3(),
                StartToLogin.newInstance("student")
            )
            "merchant" -> arrayListOf(
                MerchantOnboarding1(),
                MerchantOnboarding2(),
                MerchantOnboarding3(),
                StartToLogin.newInstance("merchant")
            )
            else -> arrayListOf()
        }

        val adapter = OnboardingAdapter(fragments, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

    }
}
