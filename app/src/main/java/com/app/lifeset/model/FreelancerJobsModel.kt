package com.app.lifeset.model

data class FreelancerJobsModel(
    val id: String,
    val job_id: String,
    val company_name: String,
    val company_mobile: String,
    val company_email: String,
    val requested_at:String,
    val request_for:String,
    var student_status:String,
    val updated_at:String,
)

data class FreelancerJobsResponse(
    val total_page: Int,
    val current_page: String,
    val total: Int,
    val msz: String,
    val status: Boolean,
    val datas:ArrayList<FreelancerJobsModel>
)

