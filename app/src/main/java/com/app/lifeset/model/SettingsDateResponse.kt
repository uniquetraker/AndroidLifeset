package com.app.lifeset.model

data class SettingsDateResponse(
    val status:Boolean,
    val msz:String,
    val data:SettingsDateModel
)
data class SettingsDateModel(
    val uid:String,
    val mobldate:String
)
