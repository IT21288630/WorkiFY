package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.workify.R
import com.example.workify.fragments.WorkerOrdersFragment
import com.example.workify.fragments.MainWorkerProfileFragment
import com.example.workify.fragments.WorkerSettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class WorkerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)

        val workerBottomNavigationView =
            findViewById<BottomNavigationView>(R.id.workerBottomNavigationView)
        val mainWorkerProfileFragment = MainWorkerProfileFragment()
        val workerSettingsFragment = WorkerSettingsFragment()

        val workerOrdersFragment = WorkerOrdersFragment()

        val mBundle = Bundle()
        mBundle.putString("curWorkerEmail", intent.getStringExtra("curWorkerEmail"))

        setCurrentFragment(mainWorkerProfileFragment, mBundle)

        workerBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miProfile -> setCurrentFragment(mainWorkerProfileFragment, mBundle)
                R.id.miSettings -> setCurrentFragment(workerSettingsFragment, mBundle)
                R.id.miNotification -> setCurrentFragment(workerOrdersFragment, mBundle)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment, mBundle: Bundle) {
        fragment.arguments = mBundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flWorkerFragment, fragment)
            commit()
        }
    }

}