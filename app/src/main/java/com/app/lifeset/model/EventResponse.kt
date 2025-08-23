package com.app.lifeset.model

data class EventResponse(
    val success: Boolean,
    val data: ArrayList<EventData>
)

data class EventData(
    val id: String? = "",
    val event_caption: String? = "",
    val title: String? = "",
    val description: String? = "",
    val state: String? = "",
    val district: String? = "",
    val created: String? = "",
    val updated: String? = "",
    val company_name: String? = "",
    val pincode: String? = "",
    val post_url: String? = "",
    val img: String? = "",
    val type: String? = "",
    val advertise_link: String? = "",

    )