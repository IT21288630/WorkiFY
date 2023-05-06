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

    inner class ViewHolder(view:android.view.View) : RecyclerView.ViewHolder(view){
        val Title: TextView
        val Description: ImageView
        val star1: ImageView
        val star2: ImageView
        val star3: ImageView
        val star4: ImageView
        val star5: ImageView
        val Reccomend: RadioButton
    }

    init{
      Title = view.findViewById()
      Description  = view.findViewById()
        Recomment = view.findViewById()
      star1 = view.findViewById ()
        star2 = view.findViewById ()
        star3 = view.findViewById ()
        star4 = view.findViewById ()
        star5 = view.findViewById ()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.WorkerReviewsForCustomerProfileAdapter, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.Title.text = data[position].workerEmail
        holder.Description.text = data[position].description
        holder.Reccomend.text = data[position].Reccoment
        when
    }







}