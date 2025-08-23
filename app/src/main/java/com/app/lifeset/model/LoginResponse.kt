package com.app.lifeset.model

data class LoginResponse(
    val response_status: Boolean,
    val msz: String,
    val datas: LoginModel
)

data class LoginModel(
    val id: Int,
    val name: String,
    val profileType: String,
    val profile_status:String,
    val college_id: String,
    val acc_token: String,
    val fcm_token: String
)