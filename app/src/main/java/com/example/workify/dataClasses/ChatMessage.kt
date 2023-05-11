package com.example.workify.dataClasses

import com.google.type.DateTime

data class ChatMessage(
    var senderEmail: String = "",
    var receiverEmail: String = "",
    var message: String = "",
    var sendDate: DateTime? = null,
)
