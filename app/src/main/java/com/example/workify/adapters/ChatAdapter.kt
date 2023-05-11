package com.example.workify.adapters

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.*
import com.example.workify.activities.ChatActivity
import com.example.workify.dataClasses.ChatMessage
import com.example.workify.dataClasses.Customer
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(
    private var data: List<ChatMessage>,
    private var context: Context,
    private var myEmail: String
) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvChatMsg: TextView
        val tvMsgSentTime: TextView


        init {
            tvChatMsg = view.findViewById(R.id.tvChatMsg)
            tvMsgSentTime = view.findViewById(R.id.tvMsgSentTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvChatMsg.text = data[position].message
        var date = SimpleDateFormat("MMMM dd - hh:mm a", Locale.getDefault()).format(data[position].date)

        holder.tvMsgSentTime.text = date.toString()

        if(myEmail == data[position].senderEmail){
            holder.tvChatMsg.background = ContextCompat.getDrawable(context, R.drawable.background_sent_message)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END
            }
            holder.tvChatMsg.layoutParams = params
            holder.tvMsgSentTime.layoutParams = params

        }else{
            holder.tvChatMsg.background = ContextCompat.getDrawable(context, R.drawable.background_receive_message)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.START
            }
            holder.tvChatMsg.layoutParams = params
            holder.tvMsgSentTime.layoutParams = params
        }


    }

    fun setData(data: List<ChatMessage>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }

}