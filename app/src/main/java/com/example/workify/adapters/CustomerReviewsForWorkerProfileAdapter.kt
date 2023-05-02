package com.example.workify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.dataClasses.CustomerReview

class CustomerReviewsForWorkerProfileAdapter(
    private var data: List<CustomerReview>,
    private var context: Context
) :
    RecyclerView.Adapter<CustomerReviewsForWorkerProfileAdapter.ViewHolder>() {

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvRCName: TextView
        val tvRCDescription: TextView
        val starOne: ImageView
        val starTwo: ImageView
        val starThree: ImageView
        val starFour: ImageView
        val starFive: ImageView

        init {
            tvRCName = view.findViewById(R.id.tvRCName)
            tvRCDescription = view.findViewById(R.id.tvRCDescription)
            starOne = view.findViewById(R.id.star1)
            starTwo = view.findViewById(R.id.star2)
            starThree = view.findViewById(R.id.star3)
            starFour = view.findViewById(R.id.star4)
            starFive = view.findViewById(R.id.star5)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.customer_review_for_worker, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvRCName.text = data[position].customer_email
        holder.tvRCDescription.text = data[position].description

        when (data[position].stars) {
            5 -> {
                holder.starOne.setImageResource(R.drawable.star)
                holder.starTwo.setImageResource(R.drawable.star)
                holder.starThree.setImageResource(R.drawable.star)
                holder.starFour.setImageResource(R.drawable.star)
                holder.starFive.setImageResource(R.drawable.star)
            }
            4 -> {
                holder.starOne.setImageResource(R.drawable.star)
                holder.starTwo.setImageResource(R.drawable.star)
                holder.starThree.setImageResource(R.drawable.star)
                holder.starFour.setImageResource(R.drawable.star)
            }
            3 -> {
                holder.starOne.setImageResource(R.drawable.star)
                holder.starTwo.setImageResource(R.drawable.star)
                holder.starThree.setImageResource(R.drawable.star)
            }
            2 -> {
                holder.starOne.setImageResource(R.drawable.star)
                holder.starTwo.setImageResource(R.drawable.star)
            }
            1 -> holder.starOne.setImageResource(R.drawable.star)
        }

    }

    fun setData(data: List<CustomerReview>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }
}