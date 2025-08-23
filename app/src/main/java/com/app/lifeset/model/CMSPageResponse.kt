package com.app.lifeset.model

data class CMSPageResponse(
    val msz: String,
    val status: Boolean,
    val datas: CMSPageModel
)

data class CMSPageModel(
    val description: String
)