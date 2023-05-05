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
import com.example.workify.activities.HomeSearchActivity
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

class WorkerPendingAdapter(
    private var data: List<Order>,
    private var context: Context
) :
    RecyclerView.Adapter<WorkerPendingAdapter.ViewHolder>() {

    private val orderCollectionRef = Firebase.firestore.collection("orders")

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val workerOrderTitle: TextView
        val AcceptbtnWorker: Button
        val RejectbtnWorker: Button
        val ViewOrderDetails:Button

        init {
            workerOrderTitle = view.findViewById(R.id.workerOrderTitle)
            AcceptbtnWorker = view.findViewById(R.id.AcceptbtnWorker)
            RejectbtnWorker = view.findViewById(R.id.RejectbtnWorker)
            ViewOrderDetails = view.findViewById(R.id.ViewOrderDetails)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_pending_order, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.workerOrderTitle.text = data[position].cusTitle

        holder.ViewOrderDetails.setOnClickListener {
            var intent = Intent(context, ViewOrderDetailsActivity::class.java)
            intent.putExtra("orderID", data[position].orderID)
            intent.putExtra("workEmail", data[position].workEmail)
            context.startActivity(intent)
        }


        holder.RejectbtnWorker.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val querySnapshot = orderCollectionRef
                        .whereEqualTo("orderID",data[position].orderID)
                        .get()
                        .await()

                    for (document in querySnapshot.documents) {
                        orderCollectionRef.document(document.id).delete()
                    }


                } catch (e: Exception) {
                    println(e.message)
                }
            }

        }

        holder.AcceptbtnWorker.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val querySnapshot = orderCollectionRef
                        .whereEqualTo("workEmail","qwe")
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
                            .update("orderStatus", "Accepted")

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