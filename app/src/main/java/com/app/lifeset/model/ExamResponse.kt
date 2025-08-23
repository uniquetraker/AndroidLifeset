package com.app.lifeset.model

data class ExamResponse(
    val success: Boolean,
    val data: ArrayList<ExamModel>
)

data class ExamModel(
    val id: String? = "",
    val post_id: String? = "",
    val cat_id: String? = "",
    val other_exam_category: String? = "",
    val exam_name: String? = "",
    val name_of_post: String? = "",
    val exam_level: String? = "",
    val other_exam_level: String? = "",
    val announcement_date: String? = "",
    val last_date_form_filling: String? = "",
    val fees: String? = "",
    val vacancy: String? = "",
    val eligibility: String? = "",
    val age_limit: String? = "",
    val description: String? = "",
    var interest_status: Int? = 0,
    val type: String? = "",
    val created_at: String? = "",
    val updated_at: String? = "",
    val advertise_link: String? = "",
    val img: String? = ""
)

data class ExamDetailResponse(
    val success: Boolean,
    val data: ExamDetailModel
)

data class ExamDetailModel(
    val id: String,
    val post_id: String,
    val cat_id: String,
    val other_exam_category: String,
    val exam_name: String,
    val name_of_post: String,
    val exam_level: String,
    val other_exam_level: String,
    val announcement_date: String,
    val last_date_form_filling: String,
    val fees: String,
    val vacancy: String,
    val eligibility: String,
    val age_limit: String,
    val description: String,
    var interest_status: Int
)
