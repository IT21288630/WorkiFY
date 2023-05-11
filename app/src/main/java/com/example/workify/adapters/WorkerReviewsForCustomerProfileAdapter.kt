package com.example.workify.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.activities.CustomerEditReviewActivity
import com.example.workify.activities.ViewCusOrderDetailsActivity
import com.example.workify.dataClasses.Order
import com.example.workify.dataClasses.Review
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



class WorkerReviewsForCustomerProfileAdapter(
    private var data: List<Review>,
    private var Context: Context) : RecyclerView.Adapter<WorkerReviewsForCustomerProfileAdapter.ViewHolder>(){

    private val rewCollectionRef = Firebase.firestore.collection("worker_reviews")


    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        //val sellers_review_name_on_cus_profile: TextView
        val sellers_review_title_on_cus_profile: TextView
        val starsInCustomer: TextView

        val ShowreviewcutomerViewBtn: Button
        val ShowreviewcutomerEditBtn: Button
        val ShowreviewcutomerDeleteBtn: Button


        init {
            //sellers_review_name_on_cus_profile = view.findViewById(R.id.sellers_review_title_on_cus_profile)
            sellers_review_title_on_cus_profile = view.findViewById(R.id.sellers_review_title_on_cus_profile)
            starsInCustomer = view.findViewById(R.id.starsInCustomer)

            ShowreviewcutomerViewBtn = view.findViewById(R.id.ShowreviewcutomerViewBtn)
            ShowreviewcutomerEditBtn = view.findViewById(R.id.ShowreviewcutomerEditBtn)
            ShowreviewcutomerDeleteBtn= view.findViewById(R.id.ShowreviewcutomerDeleteBtn)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.customer_show_reviews_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.sellers_review_title_on_cus_profile.text = data[position].title

        holder.ShowreviewcutomerViewBtn.setOnClickListener {
            var intent = Intent(Context, CustomerEditReviewActivity::class.java)
            intent.putExtra("customer_email", data[position].customer_email)
            intent.putExtra("worker_email", data[position].worker_email)
            Context.startActivity(intent)
        }


        holder.ShowreviewcutomerViewBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val querySnapshot = rewCollectionRef
                        .whereEqualTo("customer_email",data[position].customer_email)
                        .get()
                        .await()

                    for (document in querySnapshot.documents) {
                        rewCollectionRef.document(document.id).delete()
                    }


                } catch (e: Exception) {
                    println(e.message)
                }
            }

        }


    }
    fun setData(data: List<Review>, context: Context) {
        this.data = data
        this.Context = context
        notifyDataSetChanged()
    }



}