package com.app.lifeset.model

data class JobDetailResponse(
    val msz: String,
    val status: Boolean,
    val datas: JobDetailModel
)

data class JobDetailModel(
    val id: Int,
    val post_type: Int,
    val type: String,
    val title: String,
    val posttitle: String,
    val post_for: String,
    val post_by: String,
    val industry: String,
    val functions: String,
    val role: String,
    val experience: String,
    val job_location: String,
    val skill: String,
    val jb_type: String,
    val client_manage: String,
    val capacity: String,
    val fixed_salary: String,
    val working_day: String,
    val work_time: String,
    val dob: String,
    val description: String,
    var is_applied: String,
    var is_bookmarked: String,
    val post_category: String
)