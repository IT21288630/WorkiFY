package com.example.workify.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.WorkerServicesAdapter
import com.example.workify.dataClasses.Category
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class WorkerProfileServicesFragment : Fragment(R.layout.fragment_worker_profile_services) {

    private val workerCatCollectionRef = Firebase.firestore.collection("worker_cat")
    private val categoryCollectionRef = Firebase.firestore.collection("categories")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvWorkerServices)

        var services = mutableListOf<Category>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCatCollectionRef
                    .whereEqualTo("wEmail", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val name = document.get("cName").toString()
                    val price = document.get("hrRate").toString()
                    val description = document.get("wDesc").toString()

                    val querySnapshot2 = categoryCollectionRef
                        .whereEqualTo("name", name)
                        .get()
                        .await()

                    var image = ""

                    for (doc in querySnapshot2.documents) {
                        image = doc.get("image").toString()
                    }

                    val category = Category(name, description, image, price)

                    services.add(category)
                }

                withContext(Dispatchers.Main) {
                    val adapter = WorkerServicesAdapter(services, view.context)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view.context)
                    adapter.setData(services, view.context)
                }


            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

}