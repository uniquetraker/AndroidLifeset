package com.app.lifeset.model

data class CollageResponse(
    val status: Boolean,
    val msz: String,
    val datas: ArrayList<CollageModel>
)

data class CollageModel(
    val id: Int,
    val name: String
)
