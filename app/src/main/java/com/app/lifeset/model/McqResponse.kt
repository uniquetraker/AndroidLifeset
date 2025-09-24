package com.app.lifeset.model

data class McqResponse(
    val status: Boolean,
    val message: String,
    val data: ArrayList<McqModel>
)

data class McqModel(
    val id: String,
    val title: String,
    val objquestions: String,
    val answer: String,
    val right_answer: String,
    val type: String? = "",
    val advertise_link: String? = "",
    val img: String? = "",
    val updated: String? = "",
    val category_name: String? = "",
    val sub_category_name: String? = "",
    val section_name: String? = "",
)
