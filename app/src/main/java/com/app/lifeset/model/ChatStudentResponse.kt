package com.app.lifeset.model

data class ChatStudentResponse(
    val total_page: Int,
    val current_page: Int,
    val total: Int,
    val status: Boolean,
    val datas: ArrayList<ChatStudentModel>
)

data class ChatStudentModel(
    val id: Int? = 0,
    val name: String? = "",
    val email: String? = "",
    val mobile: String? = "",
    val study_year: String? = "",
    val profile: String? = "",
    val study_at: String? = "",
    val location: String? = "",
    val school_name: String? = "",
    val school_district: String? = "",
    val course_name: String? = "",
    val interest: String? = "",
    val skills: String? = "",
    var status: String? = "",
    val type: String? = "",
    val advertise_link: String? = "",
    val img: String? = ""
)
