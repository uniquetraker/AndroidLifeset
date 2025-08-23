package com.app.lifeset.model

data class PersonalityResponse(
    val success: Boolean,
    val data: ArrayList<PersonalityModel>

)

data class PersonalityModel(
    val id: String? = "",
    val name: String? = "",
    val image: String? = "",
    val post_type: String? = "",
    var personality_answer: String? = "",
    val category: String? = "",
    val img: String? = "",
    val advertise_link: String? = ""
)
