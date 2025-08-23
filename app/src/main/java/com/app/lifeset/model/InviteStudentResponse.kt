package com.app.lifeset.model

data class InviteStudentResponse(
    val total_page: Int,
    val current_page: Int,
    val total: Int,
    val msz: String,
    val status: Boolean,
    val datas: ArrayList<InviteStudentModel>
)

data class InviteStudentModel(
    val invite_id: Int,
    val sid: Int,
    val name: String,
    val profile: String,
    val school_name: String,
    val school_district: String,
    val study_year:String,
    val status: String,
    val study_at:String,
    val course_name:String,
    val date: String,
    val interest:String,
    val skills:String
)
