package com.example.workify.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.*
import com.example.workify.activities.BookWorkerActivity
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.Customer
import com.example.workify.dataClasses.Worker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MsgForWorkerAdapter(
    private var data: List<Customer>,
    private var context: Context,
    private var email: String
) :
    RecyclerView.Adapter<MsgForWorkerAdapter.ViewHolder>() {

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvMsgCustomerName: TextView
        val lyMsgItem: LinearLayout


        init {
            tvMsgCustomerName = view.findViewById(R.id.tvMsgCustomerName)
            lyMsgItem = view.findViewById(R.id.lyMsgItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_msg_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvMsgCustomerName.text = data[position].email

        holder.lyMsgItem.setOnClickListener {
            var intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("senderEmail", data[position].email)
            context.startActivity(intent)
        }
    }

    fun setData(data: List<Customer>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }

}