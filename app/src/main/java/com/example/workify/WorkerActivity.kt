package com.example.workify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class WorkerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)

        val workerBottomNavigationView = findViewById<BottomNavigationView>(R.id.workerBottomNavigationView)
        val mainWorkerProfileFragment = MainWorkerProfileFragment()
        val workerSettingsFragment = WorkerSettingsFragment()

        setCurrentFragment(mainWorkerProfileFragment)

        workerBottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.miProfile->setCurrentFragment(mainWorkerProfileFragment)
                R.id.miSettings->setCurrentFragment(workerSettingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flWorkerFragment, fragment)
            commit()
        }
    }

}