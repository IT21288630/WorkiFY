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
import com.example.workify.activities.HomeSearchActivity
import com.example.workify.activities.ViewCusOrderDetailsActivity
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
import kotlinx.coroutines.withContext

class CustomerPendingAdapter(
    private var data: List<Order>,
    private var context: Context,
    private var email: String
) :
    RecyclerView.Adapter<CustomerPendingAdapter.ViewHolder>() {

    private val orderCollectionRef = Firebase.firestore.collection("orders")

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val customerOrderTitle: TextView
        val customerPendingOrderId: TextView
        val CustomerViewDetailsbtn: Button
        val Customercancelorderbtn: Button


        init {
            customerOrderTitle = view.findViewById(R.id.customerOrderTitle)
            CustomerViewDetailsbtn = view.findViewById(R.id.CustomerViewDetailsbtn)
            Customercancelorderbtn = view.findViewById(R.id.Customercancelorderbtn)
            customerPendingOrderId = view.findViewById(R.id.customerPendingOrderId)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.customer_pending_order, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.customerOrderTitle.text = data[position].cusTitle
        holder.customerPendingOrderId.text = "Order ID: " + data[position].orderID

        holder.CustomerViewDetailsbtn.setOnClickListener {
            var intent = Intent(context, ViewCusOrderDetailsActivity::class.java)
            intent.putExtra("orderID", data[position].orderID)
            intent.putExtra("cusEmail", data[position].cusEmail)
            context.startActivity(intent)
        }


        holder.Customercancelorderbtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                var orders = mutableListOf<Order>()
                try {
                    val querySnapshot = orderCollectionRef
                        .whereEqualTo("orderID",data[position].orderID)
                        .get()
                        .await()

                    for (document in querySnapshot.documents) {
                        orderCollectionRef.document(document.id).delete()
                    }

                    val querySnapshot2 = orderCollectionRef
                        .whereEqualTo("cusEmail", email)
                        .whereEqualTo("orderStatus", "Pending")
                        .get()
                        .await()

                    for (document2 in querySnapshot2.documents){
                        var order = document2.toObject<Order>()

                        if (order != null) {
                            orders.add(order)
                        }
                    }

                    withContext(Dispatchers.Main){
                        setData(orders, context)
                    }


                } catch (e: Exception) {
                    println(e.message)
                }
            }

        }




    }

    fun setData(data: List<Order>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }
}