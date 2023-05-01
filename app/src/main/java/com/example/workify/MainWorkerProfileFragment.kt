package com.example.workify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button

/**
 * A simple [Fragment] subclass.
 */
class MainWorkerProfileFragment : Fragment(R.layout.fragment_main_worker_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workerProfileDescriptionFragment = WorkerProfileDescriptionFragment()
        val workerProfileServicesFragment = WorkerProfileServicesFragment()
        val workerProfileReviewsFragment = WorkerProfileReviewsFragment()

        childFragmentManager.beginTransaction().apply {
            replace(R.id.flWorkerProfileFragment, workerProfileDescriptionFragment)
            commit()
        }

        val btnDescription = view.findViewById<Button>(R.id.btnDescription)
        val btnServices = view.findViewById<Button>(R.id.btnServices)
        val btnReviews = view.findViewById<Button>(R.id.btnReviews)

        btnDescription.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileDescriptionFragment)
                addToBackStack(null)
                commit()
            }
        }

        btnServices.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileServicesFragment)
                addToBackStack(null)
                commit()
            }
        }

        btnReviews.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileReviewsFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

}