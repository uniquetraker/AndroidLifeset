package com.app.lifeset.model

data class HomeTownRequest(
    val id: String,
    val action: String,
    val state: String,
    val district: String,
    val city: String,
    val pincode: String,
    val address: String
)