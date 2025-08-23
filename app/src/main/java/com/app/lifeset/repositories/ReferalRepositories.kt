package com.app.lifeset.repositories

import com.app.lifeset.model.AddReferalRequest
import com.app.lifeset.network.ApiService
import javax.inject.Inject

class ReferalRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun getReferalList(stuId: String) = apiService.getReferalList(stuId)
    suspend fun addReferal(request: AddReferalRequest)=apiService.addReferal(request)
    suspend fun deleteReferal(stdId:String,id:String)=apiService.deleteReferal(stdId,id)

}