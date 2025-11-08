package com.app.lifeset.repositories

import com.app.lifeset.model.McqRequest
import com.app.lifeset.network.ApiService
import javax.inject.Inject

class McqRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun getMcqData(language:String,uid: String)=apiService.getMcqData(language,uid)
    suspend fun saveMcqData(request: McqRequest)=apiService.saveMcqData(request)
}