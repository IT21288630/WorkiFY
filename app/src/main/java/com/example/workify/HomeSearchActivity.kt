package com.example.workify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_search)

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

    private fun setCurrentFragment(fragment: Fragment, mBundle: Bundle) {
        fragment.arguments = mBundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flCustomerFragment, fragment)
            commit()
        }
    }
}