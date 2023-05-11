package com.example.workify.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.dataClasses.Review

class WorkerReviewsForCustomerProfileAdapter(
    private var data: List<Review>,
    private var Context: Context) : RecyclerView.Adapter<WorkerReviewsForCustomerProfileAdapter.ViewHolder>(){



    inner class ViewHolder(view:android.view.View) : RecyclerView.ViewHolder(view) {



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_worker_profile_reviews, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       /* holder.Title.text = data[position].workerEmail

        holder.Reccomend.text = data[position].Reccomend */

    }



}