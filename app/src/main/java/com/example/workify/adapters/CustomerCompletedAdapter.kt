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
import com.example.workify.activities.CustomerReviewActivity
import com.example.workify.activities.ViewOrderDetailsActivity
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.CustomerReview
import com.example.workify.dataClasses.Order
import com.example.workify.dataClasses.Worker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CustomerCompletedAdapter(
    private var data: List<Order>,
    private var context: Context
) :
    RecyclerView.Adapter<CustomerCompletedAdapter.ViewHolder>() {

    private val orderCollectionRef = Firebase.firestore.collection("orders")

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val customerCompletedOrderTitle: TextView
        val CusCompletedOrderId : TextView
        val AddReviewCustomerCompleteOrder: Button


        init {
            customerCompletedOrderTitle = view.findViewById(R.id.customerCompletedOrderTitle)
            AddReviewCustomerCompleteOrder = view.findViewById(R.id.AddReviewCustomerCompleteOrder)
            CusCompletedOrderId = view.findViewById(R.id.CusCompletedOrderId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.customer_completed_order, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.customerCompletedOrderTitle.text = data[position].cusTitle
        holder.CusCompletedOrderId.text = "Order ID: " + data[position].orderID

        holder.AddReviewCustomerCompleteOrder.setOnClickListener {
            var intent = Intent(context, CustomerReviewActivity::class.java)
            intent.putExtra("orderID", data[position].orderID)
            intent.putExtra("cusEmail", data[position].cusEmail)
            context.startActivity(intent)
        }


    }

    fun setData(data: List<Order>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }
}