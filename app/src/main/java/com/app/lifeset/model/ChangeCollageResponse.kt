package com.app.lifeset.model

data class ChangeCollageResponse(
    val msz: String,
    val status: Boolean,
    val datas: ArrayList<ChangeCollageModel>
)

data class ChangeCollageModel(
    val name: String
)