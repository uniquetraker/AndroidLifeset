package com.app.lifeset.model

data class JobResponse(
    val currentcat: String,
    val total: Int,
    val msz: String,
    val status: Boolean,
    val datas: ArrayList<JobModel>
)

data class JobModel(
    val id: Int? = 0,
    val post_type: Int? = 0,
    val type: String? = "",
    val title: String? = "",
    val posttitle: String? = "",
    val post_for: String? = "",
    val post_by: String? = "",
    val industry: String? = "",
    val functions: String? = "",
    val role: String? = "",
    val experience: String? = "",
    val job_location: String? = "",
    val skill: String? = "",
    val job_type: String?="",
    val client_to_manage: String?="",
    val capacity: String?="",
    val fixed_salary: String?="",
    val working_days: String?="",
    val work_time: String?="",
    val dob: String?="",
    val description: String?="",
    var is_applied: String?="",
    var is_bookmarked: String?="",
    val post_category: String?="",
    val advertise_link:String?="",
    val img:String?=""
)
