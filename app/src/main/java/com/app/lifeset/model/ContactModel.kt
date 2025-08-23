package com.app.lifeset.model

data class ContactModel(
    val name: String,
    val phoneNumber: String,
    var selected: Boolean = false
)