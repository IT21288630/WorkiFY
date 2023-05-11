package com.example.workify.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.workify.R
import com.example.workify.dataClasses.Customer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CustomerDetailsFragment : Fragment(R.layout.fragment_cutomer_details) {

    private val workerCollectionRef = Firebase.firestore.collection("customers")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val bundle = arguments
        val email = bundle!!.getString("curCusEmail")

        val mBundle = Bundle()
        mBundle.putString("curCusEmail", email)





        val tvWorkerName = view.findViewById<TextView>(R.id.etWSName)
        val tvWorkerEmail = view.findViewById<TextView>(R.id.etWSEmail)
        val tvWorkerDis = view.findViewById<TextView>(R.id.etWSDistrict)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val worker = document.toObject<Customer>()

                    if (worker != null) {
                        withContext(Dispatchers.Main) {
                            tvWorkerName.text = worker.name
                            tvWorkerEmail.text = worker.email
                            tvWorkerDis.text = worker.district
                        }
                    }
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}