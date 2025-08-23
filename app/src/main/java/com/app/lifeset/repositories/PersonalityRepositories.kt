package com.app.lifeset.repositories

import com.app.lifeset.model.PersonalityRequest
import com.app.lifeset.network.ApiService
import javax.inject.Inject

class PersonalityRepositories @Inject constructor(val apiService: ApiService) {
    suspend fun getPersonalityData(uid: String) = apiService.getPersonalityData(uid)
    suspend fun doPersonalityDataResponse(request: PersonalityRequest)=apiService.doPersonalityAnswer(
        request
    )
}