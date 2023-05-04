package com.example.workify

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.adapters.CustomerReviewsForWorkerProfileAdapter
import com.example.workify.adapters.WorkerPendingAdapter
import com.example.workify.dataClasses.CustomerReview
import com.example.workify.dataClasses.Order
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
class CustomerPendingOrdersFragment : Fragment(R.layout.fragment_customer_pending_orders) {

    private val orderCollectionRef = Firebase.firestore.collection("orders")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        var orders = mutableListOf<Order>()

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCustomerPending)

        /*orderCollectionRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                println(it.message)
                return@addSnapshotListener
            }
            querySnapshot?.let {

                for (document in querySnapshot.documents) {
                    val order = document.toObject<Order>()

                    if (order != null) {
                        orders.add(order)
                    }
                }

                val adapter = WorkerPendingAdapter(orders, view.context)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(view.context)
                adapter.setData(orders, view.context)
            }
        }*/

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = orderCollectionRef
                    .whereEqualTo("workEmail", email)
                    .whereEqualTo("orderStatus", "Pending")
                    .get()
                    .await()



                for (document in querySnapshot.documents) {
                    val order = document.toObject<Order>()

                    if (order != null) {
                        orders.add(order)
                    }
                }



                withContext(Dispatchers.Main) {
                    val adapter = WorkerPendingAdapter(orders, view.context)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view.context)
                    adapter.setData(orders, view.context)


                }

            } catch (e: Exception) {
                println(e.message)
            }
        }

    }
}