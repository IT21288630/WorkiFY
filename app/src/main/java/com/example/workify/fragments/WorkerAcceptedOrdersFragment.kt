package com.example.workify.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.WorkerAcceptedAdapter
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
class WorkerAcceptedOrdersFragment : Fragment(R.layout.fragment_worker_accepted_orders) {

    private val orderCollectionRef = Firebase.firestore.collection("orders")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        var orders = mutableListOf<Order>()

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvWorkerAccepted)
        val etSearchAcceptedOrders = view.findViewById<EditText>(R.id.etSearchAcceptedOrders)

        etSearchAcceptedOrders.addTextChangedListener {
            var orderID = etSearchAcceptedOrders.text.toString()
            if (email != null) {
                searchOrders(email, recyclerView, view.context, orderID)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = orderCollectionRef
                    .whereEqualTo("workEmail", email)
                    .whereEqualTo("orderStatus", "Accepted")
                    .get()
                    .await()



                for (document in querySnapshot.documents) {
                    val order = document.toObject<Order>()

                    if (order != null) {
                        orders.add(order)
                    }
                }

                withContext(Dispatchers.Main) {
                    val adapter = email?.let { WorkerAcceptedAdapter(orders, view.context, it) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view.context)
                    adapter?.setData(orders, view.context)
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }

    }

    private fun searchOrders(
        email: String,
        recyclerView: RecyclerView,
        context: Context,
        orderID: String
    ){
        var orders = mutableListOf<Order>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot =
                    orderCollectionRef
                        .whereEqualTo("workEmail", email)
                        .whereEqualTo("orderStatus", "Accepted").get().await()

                for (document in querySnapshot.documents) {
                    var order = document.toObject<Order>()

                    if (order != null) {
                        if (order.orderID!!.lowercase().contains(orderID, ignoreCase = true)) {
                            orders.add(order)
                        }
                    }

                }

                withContext(Dispatchers.Main) {
                    val adapter = email?.let { view?.let { it1 ->
                        WorkerAcceptedAdapter(
                            orders,
                            it1.context,
                            it
                        )
                    } }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    adapter?.setData(orders, context)
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

}