package com.app.lifeset.model

data class CourseResponse(
    val status:Boolean,
    val msz:String,
    val datas:ArrayList<CourseModel>
)
data class CourseModel(
    val value:Int,
    val name:String
)
