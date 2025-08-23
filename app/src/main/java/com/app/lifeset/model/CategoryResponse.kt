package com.app.lifeset.model

data class CategoryResponse(
    val msz:String,
    val status:Boolean,
    val datas:ArrayList<CategoryModel>
)
data class CategoryModel(
    val id:Int,
    val name:String
)
