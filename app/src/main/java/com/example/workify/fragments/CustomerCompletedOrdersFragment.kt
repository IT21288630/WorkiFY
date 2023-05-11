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
import com.example.workify.adapters.CustomerCompletedAdapter
import com.example.workify.adapters.WorkerAcceptedAdapter
import com.example.workify.adapters.WorkerCompletedAdapter
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
 * Use the [CustomerCompletedOrdersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerCompletedOrdersFragment : Fragment(R.layout.fragment_customer_completed_orders) {

    private val orderCollectionRef = Firebase.firestore.collection("orders")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val email = bundle!!.getString("curCusEmail")

        var orders = mutableListOf<Order>()

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCustomerCompleted)

        val etCusSearchCompletedOrders = view.findViewById<EditText>(R.id.etCusSearchCompletedOrders)

        etCusSearchCompletedOrders.addTextChangedListener {
            var orderID = etCusSearchCompletedOrders.text.toString()
            if (email != null) {
                searchCusOrders(email, recyclerView, view.context, orderID)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = orderCollectionRef
                    .whereEqualTo("cusEmail", email)
                    .whereEqualTo("orderStatus", "Completed")
                    .get()
                    .await()



                for (document in querySnapshot.documents) {
                    val order = document.toObject<Order>()

                    if (order != null) {
                        orders.add(order)
                    }
                }

                withContext(Dispatchers.Main) {
                    val adapter = CustomerCompletedAdapter(orders, view.context)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view.context)
                    adapter.setData(orders, view.context)
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }


    }private fun searchCusOrders(
        email: String,
        recyclerView: RecyclerView,
        context: Context,
        orderID: String){
        var orders = mutableListOf<Order>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot =
                    orderCollectionRef
                        .whereEqualTo("cusEmail", email)
                        .whereEqualTo("orderStatus", "Completed").get().await()

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
                        CustomerCompletedAdapter(
                            orders,
                            it1.context

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