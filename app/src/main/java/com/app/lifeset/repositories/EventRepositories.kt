package com.app.lifeset.repositories

import com.app.lifeset.network.ApiService
import javax.inject.Inject

class EventRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun getEventData() = apiService.getEventData()
}