package com.app.lifeset.model

data class GeneralKnowledgeResponse(
    val status: Boolean,
    val message: String,
    val data: ArrayList<GeneralKnowledgeModel>
)

data class GeneralKnowledgeModel(
    val id: String,
    val title: String,
    val langu: String,
    val image: String,
    val documnet_link: String,
    val full_document_link: String? = "",
    val description: String,
    val post_type: String,
    val updated: String? = "",
    val category_name: String? = "",
    val sub_category_name: String? = "",
    val section_name: String? = "",
    val type: String? = "",
    val advertise_link: String? = "",
    val img: String? = ""
)
