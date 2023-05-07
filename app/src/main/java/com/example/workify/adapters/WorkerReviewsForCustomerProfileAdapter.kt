package com.example.workify.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.dataClasses.WorkerReview

class WorkerReviewsForCustomerProfileAdapter(
    private var data: List<WorkerReview>,
    private var Context: Context)
    : RecyclerView.Adapter<WorkerReviewsForCustomerProfileAdapter.ViewHolder>(){

    inner class ViewHolder(view:android.view.View) : RecyclerView.ViewHolder(view) {
        val Title: TextView
        val Description: ImageView
        val star1: ImageView
        val star2: ImageView
        val star3: ImageView
        val star4: ImageView
        val star5: ImageView
        val Reccomend: RadioButton


        init {
            Title = view.findViewById(R.id.tvRCName)
            Description = view.findViewById(R.id.tvRCName)
            Reccomend = view.findViewById(R.id.tvRCName)
            star1 = view.findViewById(R.id.tvRCName)
            star2 = view.findViewById(R.id.tvRCName)
            star3 = view.findViewById(R.id.tvRCName)
            star4 = view.findViewById(R.id.tvRCName)
            star5 = view.findViewById(R.id.tvRCName)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_reviews_for_customer, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.Title.text = data[position].workerEmail

        holder.Reccomend.text = data[position].Reccomend

    }



}