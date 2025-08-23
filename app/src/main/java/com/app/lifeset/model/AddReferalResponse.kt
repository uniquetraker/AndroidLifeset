package com.app.lifeset.model

data class AddReferalResponse(
    val status: String,
    val message: String,
    val skippedMessage: String? = "",
    val alreadyAdded: String? = ""
)

data class DeleteReferalResponse(
    val status: String,
    val message: String
)
