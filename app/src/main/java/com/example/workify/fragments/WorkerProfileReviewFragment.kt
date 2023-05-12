package com.example.workify.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.CustomerReviewsAdapter
import com.example.workify.adapters.WorkerReviewAdapter
import com.example.workify.adapters.WorkerServicesAdapter
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.CustomerReview
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WorkerProfileReviewFragment  : Fragment(R.layout.fragment_worker_review_list) {
    private val reviewCollectionRef = Firebase.firestore.collection("customer_reviews")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            val bundle = arguments
            val email = bundle!!.getString("curWorkerEmail")

            val recyclerView = view.findViewById<RecyclerView>(R.id.rvCustomerReviewsForSelProfile)
            var reviews = mutableListOf<CustomerReview>()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val querySnapshot = reviewCollectionRef
                        .whereEqualTo("worker_email", email)
                        .get()
                        .await()

                    for (document in querySnapshot.documents) {
                        val review = document.toObject<CustomerReview>()

                        if (review != null) {
                            reviews.add(review)
                        }
                    }

                    withContext(Dispatchers.Main) {
                        val adapter = WorkerReviewAdapter(reviews, view.context)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(view.context)
                        adapter.setData(reviews, view.context)
                    }

                } catch (e: Exception) {
                    println(e.message)
                }
            }

        }
    }