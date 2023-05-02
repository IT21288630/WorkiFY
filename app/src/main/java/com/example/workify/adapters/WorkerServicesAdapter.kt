package com.example.workify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.CustomerReview
import com.squareup.picasso.Picasso

class WorkerServicesAdapter(
    private var data: List<Category>,
    private var context: Context
) :
    RecyclerView.Adapter<WorkerServicesAdapter.ViewHolder>() {

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val ivServiceImage: ImageView
        val tvServiceName: TextView
        val tvServiceDescription: TextView
        val tvServiceRate: TextView

        init {
            ivServiceImage = view.findViewById(R.id.ivServiceImage)
            tvServiceName = view.findViewById(R.id.tvServiceName)
            tvServiceDescription = view.findViewById(R.id.tvServiceDescription)
            tvServiceRate = view.findViewById(R.id.tvServiceRate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_service, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvServiceName.text = data[position].name
        holder.tvServiceDescription.text = data[position].description
        holder.tvServiceRate.text = "RS" + data[position].price + "/Hr"
        Picasso.get().load(data[position].image).into(holder.ivServiceImage)
    }

    fun setData(data: List<Category>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }
}