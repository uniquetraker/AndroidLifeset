package com.app.lifeset.repositories

import com.app.lifeset.network.ApiService
import javax.inject.Inject

class McqRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun getMcqData(language:String)=apiService.getMcqData(language)
}