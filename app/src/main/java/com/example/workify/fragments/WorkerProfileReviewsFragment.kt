package com.example.workify.fragments
/*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.cust_revi_on_workerAdapter
import com.example.workify.dataClasses.CustomerReview
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
class WorkerProfileReviewsFragment : Fragment(R.layout.fragment_worker_profile_reviews) {

    private val reviewCollectionRef = Firebase.firestore.collection("customer_reviews")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCustomerReviewsForWorkerProfile)

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
                    val adapter = cust_revi_on_workerAdapter(reviews)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view.context)
                    adapter.setData(reviews, view.context)
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

}*/