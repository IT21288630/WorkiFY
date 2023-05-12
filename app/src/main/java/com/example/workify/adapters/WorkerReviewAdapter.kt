package com.example.workify.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.activities.CustomerEditReviewActivity
import com.example.workify.dataClasses.CustomerReview
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WorkerReviewAdapter (
    private var data : List<CustomerReview>,
    private var context: Context
) :
    RecyclerView.Adapter<WorkerReviewAdapter.ViewHolder>(){
    private val revCollectionRef = Firebase.firestore.collection("customer_reviews")

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val sellers_review_name_on_Sel_profile: TextView
        val sellers_review_des_on_Sel_profile: TextView
        //  val sellers_review_star_on_cus_profile: TextView
        val sellers_review_title_on_Sel_profile: TextView



        init {
            sellers_review_name_on_Sel_profile = view.findViewById(R.id.sellers_review_name_on_Sel_profile)
            sellers_review_title_on_Sel_profile = view.findViewById(R.id.sellers_review_title_on_Sel_profile)
            sellers_review_des_on_Sel_profile = view.findViewById(R.id.sellers_review_des_on_Sel_profile)
//            sellers_review_star_on_cus_profile = view.findViewById(R.id.sellers_review_star_on_sel_profile)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_show_reviews_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.sellers_review_name_on_Sel_profile.text = data[position].rev_ID
        holder.sellers_review_title_on_Sel_profile.text = data[position].title
        holder.sellers_review_des_on_Sel_profile.text = data[position].description
        //holder.sellers_review_star_on_cus_profile.text = data[position].stars.toString()

        }

    fun setData(reviews: MutableList<CustomerReview>, context: Context?) {
        this.data = data
        if (context != null) {
            this.context = context
        }
        notifyDataSetChanged()
    }
}