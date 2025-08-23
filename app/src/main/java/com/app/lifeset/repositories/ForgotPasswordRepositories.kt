package com.app.lifeset.repositories

import com.app.lifeset.network.ApiService
import javax.inject.Inject

class ForgotPasswordRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun doForgotPassword(mobile:String)=apiService.doForgotPassword(mobile)
}