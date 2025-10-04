package com.app.lifeset.repositories

import com.app.lifeset.network.ApiService
import javax.inject.Inject

class GKRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun getGKData(langu:String) = apiService.getGKData(langu)
    suspend fun getGeneralKnowledgeData(language:String)=apiService.getGeneralKnowledgeData(
        language
    )
}