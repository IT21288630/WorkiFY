package com.example.workify.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.CustomerPendingAdapter
import com.example.workify.adapters.WorkerPendingAdapter
import com.example.workify.dataClasses.Order
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
        val email = bundle!!.getString("curCusEmail")

        var orders = mutableListOf<Order>()

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCustomerPending)


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = orderCollectionRef
                    .whereEqualTo("cusEmail", email)
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
                    val adapter = CustomerPendingAdapter(orders, view.context)
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