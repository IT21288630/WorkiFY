package com.example.workify.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.activities.ViewOrderDetailsActivity
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.CustomerReview
import com.example.workify.dataClasses.Order
import com.example.workify.dataClasses.Worker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WorkerAcceptedAdapter(
    private var data: List<Order>,
    private var context: Context,
    private var email: String
) :
    RecyclerView.Adapter<WorkerAcceptedAdapter.ViewHolder>() {

    private val orderCollectionRef = Firebase.firestore.collection("orders")

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val workerOrderTitle: TextView
        val acceptedOrderId : TextView
        val CompletebtnWorker: Button
        val AcceptViewDetailsBtn: Button

        init {
            workerOrderTitle = view.findViewById(R.id.workerOrderTitle)
            acceptedOrderId = view.findViewById(R.id.acceptedOrderId)
            CompletebtnWorker = view.findViewById(R.id.CompletebtnWorker)
            AcceptViewDetailsBtn = view.findViewById(R.id.AcceptViewDetailsBtn)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_accepted_order, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.workerOrderTitle.text = data[position].cusTitle
        holder.acceptedOrderId.text = "Order ID: " + data[position].orderID

        holder.AcceptViewDetailsBtn.setOnClickListener {
            var intent = Intent(context, ViewOrderDetailsActivity::class.java)
            intent.putExtra("orderID", data[position].orderID)
            intent.putExtra("workEmail", data[position].workEmail)
            context.startActivity(intent)
        }

        holder.CompletebtnWorker.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                var orders = mutableListOf<Order>()
                try {
                    val querySnapshot = orderCollectionRef
                        .whereEqualTo("workEmail",email)
                        .whereEqualTo("orderID",data[position].orderID)
                        .get()
                        .await()

                    println("Im inside query")

                    if(querySnapshot.isEmpty){

                        println("query empty")
                    }


                    for (document in querySnapshot.documents) {
                        println(document.get("orderID"))
                        orderCollectionRef.document(document.id)
                            .update("orderStatus", "Completed")

                    }

                    val querySnapshot2 = orderCollectionRef
                        .whereEqualTo("workEmail", email)
                        .whereEqualTo("orderStatus", "Accepted")
                        .get()
                        .await()

                    for (document2 in querySnapshot2.documents){
                        var order = document2.toObject<Order>()

                        if (order != null) {
                            orders.add(order)
                        }
                    }

                    withContext(Dispatchers.Main){
                        setData(orders, context)
                    }

                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    fun setData(data: List<Order>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }
}