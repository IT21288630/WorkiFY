package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.workify.R
import com.example.workify.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_search)

        val customerBottomNavigationView =
            findViewById<BottomNavigationView>(R.id.customerBottomNavigationView)
        val homeFragment = HomeFragment()
        val homeSearchFragment = HomeSearchFragment()
        val mainCustomerProfileFragment = MainCustomerProfileFragment()
        val customerSettingsFragment = CustomerSettingFragment()
        val customerMessagesFragment = CustomerMessagesFragment()

        val mBundle = Bundle()
        mBundle.putString("curService", intent.getStringExtra("service"))
        mBundle.putString("serviceNameFromHome", intent.getStringExtra("serviceNameFromHome"))
        mBundle.putString("curCusEmail", intent.getStringExtra("curCusEmail"))

        setCurrentFragment(homeSearchFragment, mBundle)

        customerBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miHomeCus -> setCurrentFragment(homeFragment, mBundle)
                R.id.miProfileCus -> setCurrentFragment(mainCustomerProfileFragment, mBundle)
                R.id.miSettingsCus -> setCurrentFragment(customerSettingsFragment, mBundle)
                R.id.miMessageCus -> setCurrentFragment(customerMessagesFragment, mBundle)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment, mBundle: Bundle) {
        fragment.arguments = mBundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flHomeSearchFragment, fragment)
            commit()
        }
    }
}