package com.app.lifeset.model.chat

data class ChatResponse(
    val total_page: Int,
    val current_page: String,
    val total: Int,
    val msz: String,
    val status: Boolean,
    val datas: ArrayList<ChatModel>
)

data class ChatModel(
    val id: String,
    val sender_id: String,
    val receiver_id: String,
    val name: String,
    val profile: String,
    val comments: String,
    val date: String,
)
