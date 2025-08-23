package com.app.lifeset.repositories

import com.app.lifeset.network.ApiService
import javax.inject.Inject

class JobRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun getJobList(jb_type: String, uid: String) = apiService.getJobList(jb_type, uid)

    suspend fun getJobDetail(uid: String, id: String) = apiService.getJobDetail(uid, id)

    suspend fun applyPost(
        collageId: String, uid: String,
        postId: String
    ) = apiService.applyPost(

        collageId, uid, postId
    )

    suspend fun getAppliedJobs(collegeId: String, uid: String) = apiService.getAppliedJobs(
        collegeId, uid
    )

    suspend fun addBookMark(collegeId: String, uid: String, postId: String) =
        apiService.addBookMark(
            collegeId, uid, postId
        )
    suspend fun getSettingsMobileDateResponse(uid:String)=apiService.getSettingsMobileUpdatedDate(uid)
}