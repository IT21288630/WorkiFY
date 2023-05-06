package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.workify.R
import com.example.workify.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        val customerBottomNavigationView =
            findViewById<BottomNavigationView>(R.id.customerBottomNavigationView)
        val homeFragment = HomeFragment()
        val mainCustomerProfileFragment = MainCustomerProfileFragment()
        val customerSettingsFragment = CustomerSettingFragment()

        val mBundle = Bundle()
        mBundle.putString("curCusEmail", intent.getStringExtra("curCusEmail"))

        setCurrentFragment(mainCustomerProfileFragment, mBundle)

        customerBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miHomeCus -> setCurrentFragment(homeFragment, mBundle)
                R.id.miProfile -> setCurrentFragment(mainCustomerProfileFragment, mBundle)
                R.id.miSettings -> setCurrentFragment(customerSettingsFragment, mBundle)
            }
            true
        }
    }

    fun setCurrentFragment(fragment: Fragment, mBundle: Bundle) {
        fragment.arguments = mBundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flCustomerFragment, fragment)
            commit()
        }
    }
}