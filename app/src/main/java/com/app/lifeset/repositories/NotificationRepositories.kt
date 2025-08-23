package com.app.lifeset.repositories

import com.app.lifeset.network.ApiService
import javax.inject.Inject

class NotificationRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun getNotifications(mobile: String) = apiService.getNotificationList(mobile)

    suspend fun deleteNotifications(id: String, mobile: String) =
        apiService.deleteNotifications(id, mobile)
}