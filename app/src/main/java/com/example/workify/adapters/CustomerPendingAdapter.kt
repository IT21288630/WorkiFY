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
import com.example.workify.dataClasses.Order
import com.squareup.picasso.Picasso

class CustomerPendingAdapter(
    private var data: List<Order>,
    private var context: Context
) :
    RecyclerView.Adapter<CustomerPendingAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }
    inner class ViewHolder(view: android.view.View,clickListener: onItemClickListener) : RecyclerView.ViewHolder(view) {
        val customerOrderTitle: TextView

        init {
            view.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }

        init {
            customerOrderTitle = view.findViewById(R.id.workerOrderTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_pending_order, parent, false)

        return ViewHolder(view,mListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.customerOrderTitle.text = data[position].cusTitle
    }

    fun setData(data: List<Order>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }
}