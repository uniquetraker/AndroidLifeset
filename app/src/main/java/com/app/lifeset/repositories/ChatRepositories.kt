package com.app.lifeset.repositories

import com.app.lifeset.model.SaveFreelancerRequest
import com.app.lifeset.network.ApiService
import javax.inject.Inject

class ChatRepositories @Inject constructor(val apiService: ApiService) {
    suspend fun getChatStudent(
        uid: String, search: String,
        page: String
    ) = apiService.getChatStudents(
        uid, search, page
    )

    suspend fun inviteStudent(
        uid: String, requestTo: String
    ) = apiService.inviteStudent(uid, requestTo)

    suspend fun getInviteList(uid: String, page: String) = apiService.getInviteList(
        uid, page
    )

    suspend fun getMyNetworks(
        uid: String, requestTo: String
    ) = apiService.getMyNetworks(uid, requestTo)


    suspend fun inviteStatus(
        uid: String,
        invite_id: String,
        status: String
    ) = apiService.inviteStatus(
        uid, invite_id, status
    )

    suspend fun getChat(uid: String, page: String) = apiService.getChat(
        uid, page
    )

    suspend fun getChatStudentHistory(
        uid: String,
        receiverId: String,
        page: String
    ) = apiService.getStudentChatHistory(
        uid, receiverId, page
    )

    suspend fun sendMessage(
        uid: String,
        receiverId: String, comments: String
    ) = apiService.sendMessage(
        uid, receiverId, comments
    )

    suspend fun getFeelancerJobs(
        uid: String, search: String,
        page: String
    ) = apiService.getFreelancerJobs(
        uid, search, page
    )

    suspend fun getFreelancerStatus() = apiService.getFreelancerStatus()
    suspend fun saveFreelancerDetails(request: SaveFreelancerRequest)
    =apiService.saveFreelancerDetails(request)

    suspend fun updateJobStatus(
        uid:String,
        id:String,
        status:String
    )=apiService.updateJobStatus(
        uid.toString(),id,status
    )
}