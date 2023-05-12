package com.example.workify.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.MsgForWorkerAdapter
import com.example.workify.dataClasses.Customer
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
class WorkerMessagesFragment : Fragment(R.layout.fragment_worker_messages) {

    private val customerWorkerMsgCollectionRef = Firebase.firestore.collection("customer_worker_msg")
    private val customerCollectionRef = Firebase.firestore.collection("customers")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        var customers = mutableListOf<Customer>()

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvWorkerMsg)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = customerWorkerMsgCollectionRef
                    .whereEqualTo("wEmail", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val customer = Customer()

                    customer.email = document.get("cEmail").toString()

                    val querySnapshot2 = customerCollectionRef
                        .whereEqualTo("email", customer.email)
                        .get()
                        .await()

                    for (document2 in querySnapshot2.documents) {
                        customer.name = document2.get("name").toString()
                    }
                    customers.add(customer)
                }

                withContext(Dispatchers.Main) {
                    val adapter = email?.let { MsgForWorkerAdapter(customers, view.context, it) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    adapter?.setData(customers, view.context)
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}