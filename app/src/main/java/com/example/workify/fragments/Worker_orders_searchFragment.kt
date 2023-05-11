package com.example.workify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.HomeSearchAdapter
import com.example.workify.adapters.WorkerOrdersSearchAdapter
import com.example.workify.dataClasses.Order
import com.example.workify.dataClasses.Worker
import com.google.firebase.firestore.QuerySnapshot
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
 * Use the [worker_orders_searchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
/*class worker_orders_searchFragment : Fragment() {

    private val orderCollectionRef = Firebase.firestore.collection("orders")

    private var cusTitle = true



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = view.findViewById<SearchView>(R.id.searchView)



        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")
        var orderTitle: String? = bundle!!.getString("cusOrderTitle")

        var orders = mutableListOf<Order>()

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvOrdersSearchResult)


        if(orderTitle != null){
            searchByName(recyclerView,searchView)
        }

        curWorkerEmail?.let { initSearch(it, recyclerView) }


    }

    fun initSearch(cusTitle: String, recyclerView: RecyclerView) {
        var orders = mutableListOf<Order>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot =
                    orderCollectionRef.whereEqualTo("cusTitle", cusTitle).get().await()

                for (document in querySnapshot.documents) {
                    val querySnapshot2 =
                        orderCollectionRef.whereEqualTo("email", document.get("workEmail")).get()
                            .await()

                    for (document2 in querySnapshot2.documents) {
                        val order = document2.toObject<Order>()
                        order?.cusTitle = document.get("cusTitle") as String?

                        if (order != null) {
                            orders.add(order)
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    val adapter = view?.let { WorkerOrdersSearchAdapter(orders, it.context) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view?.context)
                    view?.context?.let { adapter?.setData(orders, it) }
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun searchByName(recyclerView: RecyclerView,cusTitle: String) {
        var orders = mutableListOf<Order>()

        CoroutineScope(Dispatchers.IO).launch {
            try {

                var querySnapshot: QuerySnapshot? = if (!cusTitle) {
                    orderCollectionRef.whereEqualTo("cusTitle", cusTitle).get().await()
                } else {
                    orderCollectionRef.get().await()
                }

                for (document in querySnapshot?.documents!!) {
                    val querySnapshot2 =
                        orderCollectionRef.whereEqualTo("email", document.get("workEmail")).get()
                            .await()

                    for (document2 in querySnapshot2.documents) {
                        val order = document2.toObject<Order>()
                        order?.cusTitle = document.get("cusTitle") as String?

                        if (order != null) {
                            if(order.cusTitle.lowercase().contains(cusTitle, ignoreCase = true)){
                                orders.add(order)
                            }
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    val adapter = view?.let { WorkerOrdersSearchAdapter(orders, it.context) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view?.context)
                    view?.context?.let { adapter?.setData(orders, it) }

                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

}
*/
