package com.app.lifeset.model

import com.google.gson.annotations.SerializedName

data class StudentProfileResponse(
    val msz: String,
    val status: Boolean,
    val datas: StudentProfileModel,
    val coursedetail: ArrayList<CourseDetail>

)

data class StudentProfileModel(
    val course: String,
    val school_name:String,
    val year: String,
    val semester: String,
    val profile: String,
    val freelancer_detail:String,
    val name: String,
    val college_id:String?="",
    val email: String,
    val mobile: String,
    val gender: String,
    val lang_know: String,
    val caste: String,
    val religion: String,
    val state: String,
    val village: String,
    val district: String,
    val city: String,
    val address: String,
    val pincode: String,
    val interest:String,
    val skills:String,
    val is_freelancer:Int,

    @SerializedName("12_board")
    val twelveBoard: String,

    @SerializedName("12_sc_location")
    val twelveLocation: String,


    @SerializedName("12_passing_year")
    val twelvePassingYear: String,

    @SerializedName("12_aggeregate")
    val twelvePercentage: String,


    @SerializedName("10_board")
    val tenBoard: String,

    @SerializedName("10_sc_location")
    val tenLocation: String,


    @SerializedName("10_passing_year")
    val tenPassingYear: String,

    @SerializedName("10_aggeregate")
    val tenPercentage: String,

    val tech_skills: String,
    val prof_skills: String,
    val soft_skills: String,
    val hobbies: String,
    val edu_year: String,
    val edu_passyear: String,
    val edu_percentage: String
)

data class CourseDetail(
    val edu_year: String,
    val edu_passyear: String,
    val edu_percentage: String
)

