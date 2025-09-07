package com.app.lifeset.model

import com.google.gson.annotations.SerializedName

data class EducationInformationRequest(
    val id: String,
    val action: String,
    @SerializedName("12_board")
    val board: String? = "",
    @SerializedName("12_sc_location")
    val location: String? = "",
    @SerializedName("12_passing_year")
    val passingYear: String? = "",
    @SerializedName("12_aggeregate")
    val aggregate: String? = "",
    @SerializedName("10_board")
    val tenboard: String? = "",
    @SerializedName("10_sc_location")
    val tenlocation: String? = "",
    @SerializedName("10_passing_year")
    val tenpassingYear: String? = "",
    @SerializedName("10_aggeregate")
    val tenaggregate: String? = "",
    val edu_year: Array<String>,
    val edu_passyear: Array<String>,
    val edu_percentage: Array<String>,
    val year:String,
    val course:String


    )