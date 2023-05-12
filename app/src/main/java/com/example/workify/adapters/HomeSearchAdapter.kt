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
import com.example.workify.activities.ChatActivity
import com.example.workify.activities.HomeSearchActivity
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.CustomerReview
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

class HomeSearchAdapter(
    private var data: List<Worker>,
    private var context: Context,
    private var email: String
) :

    RecyclerView.Adapter<HomeSearchAdapter.ViewHolder>() {

    private val customerWorkerMsgCollectionRef = Firebase.firestore.collection("customer_worker_msg")

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvSearchWName: TextView
        val tvSearchWRate: TextView
        val orderWorkerBtn: Button
        val ivSendMsg: ImageView

        init {
            tvSearchWName = view.findViewById(R.id.tvSearchWName)
            tvSearchWRate = view.findViewById(R.id.tvSearchWRate)
            orderWorkerBtn = view.findViewById(R.id.orderWorkerBtn)
            ivSendMsg = view.findViewById(R.id.ivSendMsg)
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
            intent.putExtra("cusEmail", email)
            context.startActivity(intent)
        }

        holder.ivSendMsg.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val querySnapshot = customerWorkerMsgCollectionRef
                        .whereEqualTo("wEmail", data[position].email)
                        .whereEqualTo("cEmail", email)
                        .get()
                        .await()

                    if (querySnapshot.isEmpty){
                        val map = mutableMapOf<String, Any>()

                        map["cEmail"] = email
                        map["wEmail"] = data[position].email

                        val querySnapshot2 = customerWorkerMsgCollectionRef.add(map)
                            .await()
                    }

                } catch (e: Exception) {
                    println(e.message)
                }
            }

            var intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("senderEmail", data[position].email)
            intent.putExtra("receiverEmail", email)
            intent.putExtra("wEmail", data[position].email)
            intent.putExtra("cEmail", email)
            intent.putExtra("senderName", data[position].name)
            context.startActivity(intent)
        }
    }

    fun setData(data: List<Worker>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }
}