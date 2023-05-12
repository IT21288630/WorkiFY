package com.example.workify.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.workify.R
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
    private val revCollectionRef = Firebase.firestore.collection("customer_reviews")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workerProfileDescriptionFragment = WorkerProfileDescriptionFragment()
        val workerProfileServicesFragment = WorkerProfileServicesFragment()
        val workerProfileReviewFragment = WorkerProfileReviewFragment()

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        val mBundle = Bundle()
        mBundle.putString("curWorkerEmail", bundle!!.getString("curWorkerEmail"))

        workerProfileDescriptionFragment.arguments = mBundle
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flWorkerProfileFragment, workerProfileDescriptionFragment)
            commit()
        }

        val btnDescription = view.findViewById<TextView>(R.id.btnDescription)
        val btnServices = view.findViewById<TextView>(R.id.btnServices)
        val btnReviews = view.findViewById<TextView>(R.id.btnReviews)

        btnDescription.setOnClickListener {
            btnDescription.background = ContextCompat.getDrawable(view.context, R.drawable.toggle_selected)
            btnServices.background = ContextCompat.getDrawable(view.context, R.drawable.toggle_not_selected)
            btnReviews.background = ContextCompat.getDrawable(view.context, R.drawable.toggle_not_selected)

            btnDescription.setTextColor(ContextCompat.getColor(view.context, R.color.white))
            btnServices.setTextColor(ContextCompat.getColor(view.context, R.color.newBlue))
            btnReviews.setTextColor(ContextCompat.getColor(view.context, R.color.newBlue))

            workerProfileDescriptionFragment.arguments = mBundle
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileDescriptionFragment)
                addToBackStack(null)
                commit()
            }
        }

        btnServices.setOnClickListener {
            btnDescription.background = ContextCompat.getDrawable(view.context, R.drawable.toggle_not_selected)
            btnServices.background = ContextCompat.getDrawable(view.context, R.drawable.toggle_selected)
            btnReviews.background = ContextCompat.getDrawable(view.context, R.drawable.toggle_not_selected)

            btnDescription.setTextColor(ContextCompat.getColor(view.context, R.color.newBlue))
            btnServices.setTextColor(ContextCompat.getColor(view.context, R.color.white))
            btnReviews.setTextColor(ContextCompat.getColor(view.context, R.color.newBlue))

            workerProfileServicesFragment.arguments = mBundle
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileServicesFragment)
                addToBackStack(null)
                commit()
            }
        }

        btnReviews.setOnClickListener {
            btnDescription.background = ContextCompat.getDrawable(view.context, R.drawable.toggle_not_selected)
            btnServices.background = ContextCompat.getDrawable(view.context, R.drawable.toggle_not_selected)
            btnReviews.background = ContextCompat.getDrawable(view.context, R.drawable.toggle_selected)

            btnDescription.setTextColor(ContextCompat.getColor(view.context, R.color.newBlue))
            btnServices.setTextColor(ContextCompat.getColor(view.context, R.color.newBlue))
            btnReviews.setTextColor(ContextCompat.getColor(view.context, R.color.white))

            workerProfileReviewFragment.arguments = mBundle
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flWorkerProfileFragment, workerProfileReviewFragment)
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