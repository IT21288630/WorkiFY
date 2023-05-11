package com.example.workify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.dataClasses.CustomerReview

class CustomerFavAdapter (
    private var data: List<CustomerReview>,
    private var context: Context
) :
    RecyclerView.Adapter<CustomerFavAdapter.ViewHolder>() {

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvRCName: TextView
        val tvRCDescription: TextView
        val tvRCwork: TextView
        val starOne: ImageView
        val starTwo: ImageView
        val starThree: ImageView
        val starFour: ImageView
        val starFive: ImageView

        init {
            tvRCName = view.findViewById(R.id.tvRCName)
            tvRCDescription = view.findViewById(R.id.tvRCDescription)
            tvRCwork = view.findViewById(R.id.tvRCwork)
            starOne = view.findViewById(R.id.star1)
            starTwo = view.findViewById(R.id.star2)
            starThree = view.findViewById(R.id.star3)
            starFour = view.findViewById(R.id.star4)
            starFive = view.findViewById(R.id.star5)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_fav_customer, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvRCName.text = data[position].worker_email
        holder.tvRCDescription.text = data[position].description
        holder.tvRCwork.text = data[position].title

        when (data[position].stars) {
            5 -> {
                holder.starOne.setImageResource(R.drawable.fullheart)

            }
            4 -> {
                holder.starOne.setImageResource(R.drawable.fullheart)

            }
            3 -> {
                holder.starOne.setImageResource(R.drawable.fullheart)

            }
            2 -> {
                holder.starOne.setImageResource(R.drawable.fullheart)

            }
            1 -> holder.starOne.setImageResource(R.drawable.fullheart)
        }

    }
   /* fullheart*/
        fun setData(data: List<CustomerReview>, context: Context) {
            this.data = data
            this.context = context
            notifyDataSetChanged()
        }

    }