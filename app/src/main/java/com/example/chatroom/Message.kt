package com.example.chatroom


data class Message(
    val id: String = "",
    val text: String = "",
    val senderId: String = "",
    val senderFirstName: String = "",
    val timestamp: Long = 0L,
    var isSentByCurrentUser: Boolean = false
)

