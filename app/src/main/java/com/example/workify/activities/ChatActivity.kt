package com.example.workify.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.ChatAdapter
import com.example.workify.dataClasses.ChatMessage
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

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
        var senderName = intent.getStringExtra("senderName")

        val tvMsgSenderEmail = findViewById<TextView>(R.id.tvMsgSenderEmail)
        val rvChatMsgs = findViewById<RecyclerView>(R.id.rvChatMsgs)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val ivMsgSent = findViewById<ImageView>(R.id.ivMsgSent)
        val ivMsgBack = findViewById<ImageView>(R.id.ivMsgBack)

        tvMsgSenderEmail.text = senderName

        ivMsgBack.setOnClickListener {
            finish()
        }

        if (wEmail != null && cEmail != null && myEmail != null) {
            //getMessages(rvChatMsgs, wEmail, cEmail, myEmail)
            realTime(rvChatMsgs, wEmail, cEmail, myEmail)
        }

        ivMsgSent.setOnClickListener {
            if (etMessage.text.toString().isEmpty()) {
                return@setOnClickListener
            }

            var chatMsg: ChatMessage? = null

            if (otherEmail != null && myEmail != null) {
                chatMsg = ChatMessage(
                    myEmail,
                    otherEmail,
                    etMessage.text.toString(),
                    Date(),
                    wEmail,
                    cEmail
                )
            }

            etMessage.text = null

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (chatMsg != null) {
                        chatCollectionRef.add(chatMsg).await()
                    }



                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ChatActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun getMessages(
        recyclerView: RecyclerView,
        wEmail: String,
        cEmail: String,
        myEmail: String
    ) {
        chats.clear()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = chatCollectionRef
                    .whereEqualTo("wemail", wEmail)
                    .whereEqualTo("cemail", cEmail)
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
                    recyclerView.smoothScrollToPosition(chats.size - 1)
                }


            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun realTime(
        recyclerView: RecyclerView,
        wEmail: String,
        cEmail: String,
        myEmail: String
    ) {
        chats.clear()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = chatCollectionRef
                    .whereEqualTo("wemail", wEmail)
                    .whereEqualTo("cemail", cEmail)
                    .orderBy("date", Query.Direction.ASCENDING)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            return@addSnapshotListener
                        }
                        if (value != null) {
                            var count = chats.size
                            for (documentChange: DocumentChange in value.documentChanges){
                                if (documentChange.type == DocumentChange.Type.ADDED){
                                    var chatMessage = documentChange.document.toObject<ChatMessage>()

                                    chats.add(chatMessage)
                                }
                            }

                            val adapter = ChatAdapter(chats, this@ChatActivity, myEmail)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(this@ChatActivity)
                            adapter.setData(chats, this@ChatActivity)
                            if (chats.size == 0){
                                recyclerView.smoothScrollToPosition(0)
                            }else{
                                recyclerView.smoothScrollToPosition(chats.size - 1)
                            }

                        }
                    }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

}