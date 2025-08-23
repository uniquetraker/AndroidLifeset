package com.app.lifeset.repositories

import com.app.lifeset.network.ApiService
import javax.inject.Inject

class ChangePasswordRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun doChangePassword(uid:String,cnfPassword:String) = apiService.doChangePassword(
        uid,cnfPassword
    )
}