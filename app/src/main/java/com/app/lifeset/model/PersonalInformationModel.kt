package com.app.lifeset.model

data class PersonalInformationModel(
    val id: String,
    val action: String,
    val name: String,
    val email: String,
    val contact_number: String,
    val gender: String,
    val caste: String,
    val religion: String
)