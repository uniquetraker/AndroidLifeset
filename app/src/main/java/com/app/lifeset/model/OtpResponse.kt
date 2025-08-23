package com.app.lifeset.model

data class OtpResponse(
    val datas: OtpModel,
    val msz: String,
    val status: Boolean
)

data class OtpModel(
    val id: Int,
    val name: String,
    val profileType: String,
    val college_id: String,
    val fcm_token: String,
    val acc_token: String
)