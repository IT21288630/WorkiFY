package com.example.workify.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.*
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.CustomerReview
import com.squareup.picasso.Picasso

class ServicesForHomeAdapter(
    private var data: List<Category>,
    private var context: Context
) :
    RecyclerView.Adapter<ServicesForHomeAdapter.ViewHolder>() {

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val ivServiceHome: ImageView
        val tvServiceHome: TextView

        init {
            ivServiceHome = view.findViewById(R.id.ivServiceHome)
            tvServiceHome = view.findViewById(R.id.tvServiceHome)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_service, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(data[position].image).into(holder.ivServiceHome)
        holder.tvServiceHome.text = data[position].name

        holder.ivServiceHome.setOnClickListener {
            var ca = CustomerActivity()
            val homeSearchFragment = HomeSearchFragment()
            val mBundle = Bundle()

            ca.setCurrentFragment(homeSearchFragment, mBundle)
        }
    }

    fun setData(data: List<Category>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }
}