package com.example.workify.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.*
import com.example.workify.activities.ChatActivity
import com.example.workify.dataClasses.Customer
import com.example.workify.dataClasses.Worker

class MsgForCustomerAdapter(
    private var data: List<Worker>,
    private var context: Context,
    private var email: String
) :
    RecyclerView.Adapter<MsgForCustomerAdapter.ViewHolder>() {

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvMsgWorkerName: TextView
        val lyMsgItem: LinearLayout


        init {
            tvMsgWorkerName = view.findViewById(R.id.tvMsgWorkerName)
            lyMsgItem = view.findViewById(R.id.lyMsgItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.customer_msg_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvMsgWorkerName.text = data[position].name

        holder.lyMsgItem.setOnClickListener {
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