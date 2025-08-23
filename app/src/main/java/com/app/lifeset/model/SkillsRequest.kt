package com.app.lifeset.model

data class SkillsRequest(
    val id: String,
    val action: String,
    val tech_skills: String,
    val prof_skills: String,
    val soft_skills: String,
    val hobbies: String
)