package com.example.workify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
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

class WorkerCompletedAdapter(
    private var data: List<Order>,
    private var context: Context
) :
    RecyclerView.Adapter<WorkerCompletedAdapter.ViewHolder>() {

    private val orderCollectionRef = Firebase.firestore.collection("orders")

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val workerOrderTitle: TextView


        init {
            workerOrderTitle = view.findViewById(R.id.workerOrderTitle)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_completed_order, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.workerOrderTitle.text = data[position].cusTitle


    }

    fun setData(data: List<Order>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }
}