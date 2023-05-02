package com.example.workify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
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
class WorkerSettingsFragment : Fragment(R.layout.fragment_worker_settings) {

    private val workerCollectionRef = Firebase.firestore.collection("workers")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        val tvWorkerName = view.findViewById<TextView>(R.id.tvWorkerNameSett)
        val tvWorkerEmail = view.findViewById<TextView>(R.id.tvWorkerEmailSett)
        val etWSName = view.findViewById<TextView>(R.id.etWSName)
        val etWSDistrict = view.findViewById<TextView>(R.id.etWSDistrict)
        val etWSDescription = view.findViewById<TextView>(R.id.etWSDescription)
        val btnWorkerEdit = view.findViewById<TextView>(R.id.btnWorkerEdit)

        btnWorkerEdit.setOnClickListener {
            if (email != null) {
                updateWorker(
                    email,
                    etWSName.text.toString(),
                    etWSDescription.text.toString(),
                    etWSDistrict.text.toString()
                )
            }
        }

        workerCollectionRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                println(it.message)
                return@addSnapshotListener
            }
            querySnapshot?.let {
                for (document in it) {
                    val worker = document.toObject<Worker>()

                    if (worker != null) {
                        tvWorkerName.text = worker.name
                        tvWorkerEmail.text = worker.email
                        etWSName.hint = worker.name
                        etWSDistrict.hint = worker.district
                        etWSDescription.hint = worker.description

                    }
                }
            }
        }
    }

    private fun updateWorker(email: String, name: String, description: String, district: String) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val worker = document.toObject<Worker>()

                    if (name.isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("name", name)
                    if (description.isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("description", description)
                    if (district.isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("district", district)
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
}