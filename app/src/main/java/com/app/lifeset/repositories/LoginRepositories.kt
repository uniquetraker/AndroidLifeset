package com.app.lifeset.repositories

import com.app.lifeset.network.ApiService
import javax.inject.Inject

class LoginRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun doLogin(mobile: String, password: String,fcmToken:String) = apiService.doLogin(mobile, password,
        fcmToken)

}