package com.example.workify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var customerEmail = intent.getStringExtra("senderEmail")

        val tvMsgSenderEmail = findViewById<TextView>(R.id.tvMsgSenderEmail)

        tvMsgSenderEmail.text = customerEmail

    }
}