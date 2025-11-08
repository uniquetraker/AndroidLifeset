package com.app.lifeset.model

data class SaveMcqResponse(
    val status: Boolean,
    val message: String,
    val data: McqModel
)

data class SaveMcqModel(
    val uid: String,
    val student_name: String,
    val mcq_id: String,
    val question: String,
    val right_answer: String,
    val your_response: String,
    val answer_status: Int
)
