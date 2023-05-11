package com.example.workify.dataClasses

import java.util.*

data class ChatMessage(
    var senderEmail: String = "",
    var receiverEmail: String = "",
    var message: String = "",
    var date: Date? = null,
    var wEmail: String? = "",
    var cEmail: String? = "",
    var senderName: String? = null
    )
