package com.example.workify.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.CustomerReviewsForWorkerProfileAdapter
import com.example.workify.adapters.HomeSearchAdapter
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
class HomeSearchFragment : Fragment(R.layout.fragment_home_search) {

    private val workerCatCollectionRef = Firebase.firestore.collection("worker_cat")
    private val workerCollectionRef = Firebase.firestore.collection("workers")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val curService = bundle!!.getString("curService")

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvHomeSearchResult)
        var workers = mutableListOf<Worker>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCatCollectionRef
                    .whereEqualTo("cName", curService)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val querySnapshot2 = workerCollectionRef
                        .whereEqualTo("email", document.get("wEmail"))
                        .get()
                        .await()

                    for (document2 in querySnapshot2.documents){
                        val worker = document2.toObject<Worker>()
                        worker?.price = document.get("hrRate") as String?

                        if (worker != null) {
                            workers.add(worker)
                        }
                    }
                }

                println(workers)

                withContext(Dispatchers.Main) {
                    val adapter = HomeSearchAdapter(workers, view.context)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view.context)
                    adapter.setData(workers, view.context)
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}