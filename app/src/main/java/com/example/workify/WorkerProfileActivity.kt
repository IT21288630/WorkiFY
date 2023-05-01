package com.example.workify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WorkerProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_profile)

        val workerProfileDescriptionFragment = WorkerProfileDescriptionFragment()
        val workerProfileServicesFragment = WorkerProfileServicesFragment()
        val workerProfileReviewsFragment = WorkerProfileReviewsFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flWorkerProfileFragment, workerProfileDescriptionFragment)
            commit()
        }

        val btnDescription = findViewById<Button>(R.id.btnDescription)
        val btnServices = findViewById<Button>(R.id.btnServices)
        val btnReviews = findViewById<Button>(R.id.btnReviews)

        btnDescription.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileDescriptionFragment)
                addToBackStack(null)
                commit()
            }
        }

        btnServices.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileServicesFragment)
                addToBackStack(null)
                commit()
            }
        }

        btnReviews.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileReviewsFragment)
                addToBackStack(null)
                commit()
            }
        }
    }
}