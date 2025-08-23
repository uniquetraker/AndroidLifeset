package com.app.lifeset.model.chat

data class ChatHistoryResponse(
    val datas: ArrayList<ChatHistoryModel>,
    val receiver: ReceiverModel,
    val total_page: Int,
    val current_page: String,
    val total: String,
    val msz: String,
    val status: Boolean
)

data class ChatHistoryModel(
    val id: String,
    val sender_id: String,
    val receiver_id: String,
    val comments: String,
    val date: String,
)

data class ReceiverModel(
    val sid: String,
    val name: String,
    val profile: String,
    val school_name: String,
    val school_district: String,
)
