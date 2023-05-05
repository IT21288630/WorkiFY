package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.workify.fragments.HomeSearchFragment
import com.example.workify.R
import com.example.workify.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        val customerBottomNavigationView =
            findViewById<BottomNavigationView>(R.id.customerBottomNavigationView)
        val homeFragment = HomeFragment()

        val mBundle = Bundle()
        //mBundle.putString("curWorkerEmail", intent.getStringExtra("curWorkerEmail"))

        setCurrentFragment(homeFragment, mBundle)

        customerBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miHomeCus -> setCurrentFragment(homeFragment, mBundle)
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