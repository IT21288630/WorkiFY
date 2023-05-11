package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.ChatAdapter
import com.example.workify.adapters.ServicesForSettingsAdapter
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.ChatMessage
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ChatActivity : AppCompatActivity() {

    private val chatCollectionRef = Firebase.firestore.collection("chats")
    private var chats = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var otherEmail = intent.getStringExtra("senderEmail")
        var myEmail = intent.getStringExtra("receiverEmail")
        var wEmail = intent.getStringExtra("wEmail")
        var cEmail = intent.getStringExtra("cEmail")

        val tvMsgSenderEmail = findViewById<TextView>(R.id.tvMsgSenderEmail)
        val rvChatMsgs = findViewById<RecyclerView>(R.id.rvChatMsgs)

        tvMsgSenderEmail.text = otherEmail

        if (wEmail != null && cEmail!= null && myEmail!= null) {
            getMessages(rvChatMsgs, wEmail, cEmail, myEmail)
        }
    }

    private fun getMessages(recyclerView: RecyclerView, wEmail: String, cEmail: String, myEmail: String) {
        chats.clear()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = chatCollectionRef
                    .whereEqualTo("wEmail", wEmail)
                    .whereEqualTo("cEmail", cEmail)
                    .orderBy("date", Query.Direction.ASCENDING)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val chat = document.toObject<ChatMessage>()

                    if (chat != null) {
                        chats.add(chat)
                    }
                }

                withContext(Dispatchers.Main) {
                    val adapter = ChatAdapter(chats, this@ChatActivity, myEmail)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@ChatActivity)
                    adapter.setData(chats, this@ChatActivity)
                }


            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}