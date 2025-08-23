package com.app.lifeset.model

data class CasteResponse(
    val msz: String,
    val status: Boolean,
    val relisions: ArrayList<RelisionsModel>,
    val casts: ArrayList<CastsModel>
)

data class RelisionsModel(
    val name: String
)

data class CastsModel(
    val name: String
)