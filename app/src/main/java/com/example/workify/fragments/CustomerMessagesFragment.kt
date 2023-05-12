package com.example.workify.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.MsgForCustomerAdapter
import com.example.workify.adapters.MsgForWorkerAdapter
import com.example.workify.dataClasses.Customer
import com.example.workify.dataClasses.Worker
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
class CustomerMessagesFragment : Fragment(R.layout.fragment_customer_messages) {

    private val customerWorkerMsgCollectionRef =
        Firebase.firestore.collection("customer_worker_msg")
    private val workerCollectionRef = Firebase.firestore.collection("workers")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val email = bundle!!.getString("curCusEmail")

        var workers = mutableListOf<Worker>()

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCustomerMsg)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = customerWorkerMsgCollectionRef
                    .whereEqualTo("cEmail", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val worker = Worker()

                    worker.email = document.get("wEmail").toString()

                    val querySnapshot2 = workerCollectionRef
                        .whereEqualTo("email", worker.email)
                        .get()
                        .await()

                    for (document2 in querySnapshot2.documents) {
                        worker.name = document2.get("name").toString()
                    }

                    workers.add(worker)
                }

                withContext(Dispatchers.Main) {
                    val adapter = email?.let { MsgForCustomerAdapter(workers, view.context, it) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    adapter?.setData(workers, view.context)
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

}