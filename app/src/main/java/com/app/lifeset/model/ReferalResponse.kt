package com.app.lifeset.model

import java.sql.Ref

data class ReferalResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<ReferalModel>
)

data class ReferalModel(
    val id: String,
    val name: String,
    val mobile: String,
    val date: String,
    val downloaded: String
)