package com.app.lifeset.model

data class UpdateUserResponse(
    val msz: String,
    val status: Boolean,
    val datas: UpdateUserModel
)

data class UpdateUserModel(
    val mobile: String,
    val college_id: String,
    val new_college: String,
    val stream: String,
    val course: String,
    val year: String
)