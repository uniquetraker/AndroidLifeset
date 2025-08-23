package com.app.lifeset.model

data class NotificationResponse(
    val status: String,
    val data: ArrayList<NotificationModel>
)

data class NotificationModel(
    val id: String,
    val type: String,
    val url: String,
    val contace_numbers: String,
    val message: String,
    val created: String
)