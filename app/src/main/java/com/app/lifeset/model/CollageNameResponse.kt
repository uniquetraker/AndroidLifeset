package com.app.lifeset.model

data class CollageNameResponse(
    val datas: CollageNameModel,
    val status: Boolean
)

data class CollageNameModel(
    val name: String
)