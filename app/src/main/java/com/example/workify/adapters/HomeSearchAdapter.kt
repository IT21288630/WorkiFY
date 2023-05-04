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
import com.example.workify.activities.BookWorkerActivity
import com.example.workify.activities.HomeSearchActivity
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.CustomerReview
import com.example.workify.dataClasses.Worker
import com.squareup.picasso.Picasso

class HomeSearchAdapter(
    private var data: List<Worker>,
    private var context: Context
) :
    RecyclerView.Adapter<HomeSearchAdapter.ViewHolder>() {

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvSearchWName: TextView
        val tvSearchWRate: TextView
        val orderWorkerBtn: Button

        init {
            tvSearchWName = view.findViewById(R.id.tvSearchWName)
            tvSearchWRate = view.findViewById(R.id.tvSearchWRate)
            orderWorkerBtn = view.findViewById(R.id.orderWorkerBtn)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_result_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSearchWName.text = data[position].name
        holder.tvSearchWRate.text = data[position].avgRating.toString()

        holder.orderWorkerBtn.setOnClickListener {
            var intent = Intent(context, BookWorkerActivity::class.java)
            intent.putExtra("workerEmail", data[position].email)
            context.startActivity(intent)
        }
    }

    fun setData(data: List<Worker>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }
}