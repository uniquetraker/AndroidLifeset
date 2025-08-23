package com.app.lifeset.repositories

import com.app.lifeset.model.ExamDetailRequest
import com.app.lifeset.network.ApiService
import javax.inject.Inject

class ExamRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun getExam(uid:String)=apiService.getExamData(uid)
    suspend fun postInterested(post_id:String,uid:String)=apiService.postInterested(post_id,uid)
    suspend fun getExamDetails(uid:String,post_id: String)=apiService.getExamDetails(uid,post_id)
}