package com.example.workify

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
class WorkerProfileDescriptionFragment : Fragment(R.layout.fragment_worker_profile_description) {

    private val workerCollectionRef = Firebase.firestore.collection("workers")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        val tvWorkerDescription = view.findViewById<TextView>(R.id.tvWorkerDescription)

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
                            tvWorkerDescription.text = worker.description
                        }
                    }
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}