package com.example.workify.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.activities.BookWorkerActivity
import com.example.workify.dataClasses.Order
import com.example.workify.dataClasses.Worker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class WorkerOrdersSearchAdapter (

    private var data: List<Order>,
    private var context: Context

        ):
    RecyclerView.Adapter<WorkerOrdersSearchAdapter.ViewHolder>() {



    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val SearchworkerOrderTitleResult: TextView

        init {

            SearchworkerOrderTitleResult = view.findViewById(R.id.SearchworkerOrderTitleResult)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_worker_order, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.SearchworkerOrderTitleResult.text = data[position].cusTitle


    }

    fun setData(data: List<Order>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }
}
