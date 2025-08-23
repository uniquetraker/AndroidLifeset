package com.app.lifeset.model

data class GKResponse(
    val success: Boolean,
    val data: ArrayList<GKData>
)

data class GKData(
    val id: String? = "",
    val title: String? = "",
    val post_url: String? = "",
    val pincode: String? = "",
    val state: String? = "",
    val district: String? = "",
    val hobbies: String? = "",
    val description: String? = "",
    val image: String? = "",
    val type: String? = "",
    val created_at: String? = "",
    val updated_at: String? = "",
    val advertise_link: String? = "",
    val img: String? = ""
)
