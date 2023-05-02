package com.example.workify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.workify.dataClasses.Worker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class MainWorkerProfileFragment : Fragment(R.layout.fragment_main_worker_profile) {

    private val workerCollectionRef = Firebase.firestore.collection("workers")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workerProfileDescriptionFragment = WorkerProfileDescriptionFragment()
        val workerProfileServicesFragment = WorkerProfileServicesFragment()
        val workerProfileReviewsFragment = WorkerProfileReviewsFragment()

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        val mBundle = Bundle()
        mBundle.putString("curWorkerEmail", bundle!!.getString("curWorkerEmail"))

        workerProfileDescriptionFragment.arguments = mBundle
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flWorkerProfileFragment, workerProfileDescriptionFragment)
            commit()
        }

        val btnDescription = view.findViewById<Button>(R.id.btnDescription)
        val btnServices = view.findViewById<Button>(R.id.btnServices)
        val btnReviews = view.findViewById<Button>(R.id.btnReviews)

        btnDescription.setOnClickListener {
            workerProfileDescriptionFragment.arguments = mBundle
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileDescriptionFragment)
                addToBackStack(null)
                commit()
            }
        }

        btnServices.setOnClickListener {
            workerProfileServicesFragment.arguments = mBundle
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileServicesFragment)
                addToBackStack(null)
                commit()
            }
        }

        btnReviews.setOnClickListener {
            workerProfileReviewsFragment.arguments = mBundle
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileReviewsFragment)
                addToBackStack(null)
                commit()
            }
        }

        val tvWorkerName = view.findViewById<TextView>(R.id.tvWorkerName)
        val tvWorkerEmail = view.findViewById<TextView>(R.id.tvWorkerEmail)
        val tvWorkerAvgRate = view.findViewById<TextView>(R.id.tvWorkerAvgRate)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val worker = document.toObject<Worker>()

                    if (worker != null) {
                        withContext(Dispatchers.Main){
                            tvWorkerName.text = worker.name
                            tvWorkerEmail.text = worker.email
                            tvWorkerAvgRate.text = worker.avgRating.toString()
                        }
                    }
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }

    }

}