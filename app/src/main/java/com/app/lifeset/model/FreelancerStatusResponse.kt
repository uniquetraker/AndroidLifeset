package com.app.lifeset.model

data class FreelancerStatusResponse(
    val msz: String,
    val status: Boolean,
    val datas: ArrayList<FreelancerStatusModel>
)

data class FreelancerStatusModel(
    val name: String,
    val value: String
)
