package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.model.ChatStudentResponse
import com.app.lifeset.model.FreelancerJobsResponse
import com.app.lifeset.model.FreelancerStatusResponse
import com.app.lifeset.model.InviteStudentResponse
import com.app.lifeset.model.SaveFreelancerDetailResponse
import com.app.lifeset.model.SaveFreelancerRequest
import com.app.lifeset.model.chat.ChatHistoryResponse
import com.app.lifeset.model.chat.ChatResponse
import com.app.lifeset.repositories.ChatRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(val repositories: ChatRepositories) : ViewModel() {

    val isLoading = MutableLiveData(false)
    val chatLiveData = MutableLiveData<ChatStudentResponse>()
    val inviteStudentLiveData = MutableLiveData<BaseResponse>()
    val myNetworkLiveData = MutableLiveData<InviteStudentResponse>()
    val inviteListLiveData = MutableLiveData<InviteStudentResponse>()
    val inviteStatusLiveData = MutableLiveData<BaseResponse>()
    val getChatLiveData = MutableLiveData<ChatResponse>()
    val chatHistoryLiveData = MutableLiveData<ChatHistoryResponse>()
    val sendMessageLiveData = MutableLiveData<BaseResponse>()
    val freelancerJobsLiveData = MutableLiveData<FreelancerJobsResponse>()
    val freelancerStatusLiveData = MutableLiveData<FreelancerStatusResponse>()
    val saveFreelancerLiveData = MutableLiveData<SaveFreelancerDetailResponse>()
    val freelancerUpdateStatusLiveData = MutableLiveData<BaseResponse>()

    fun getChatStudent(uid: String, search: String, page: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getChatStudent(uid, search, page).let {
                if (it.body() != null) {
                    chatLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun inviteStudent(uid: String, requestTo: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.inviteStudent(uid, requestTo).let {
                if (it.body() != null) {
                    inviteStudentLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun getInviteList(uid: String, page: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getInviteList(uid, page).let {
                if (it.body() != null) {
                    inviteListLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun getMyNetworkList(uid: String, page: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getMyNetworks(uid, page).let {
                if (it.body() != null) {
                    myNetworkLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun inviteStatus(uid: String, invite_id: String, status: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.inviteStatus(uid, invite_id, status).let {
                if (it.body() != null) {
                    inviteStatusLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun getChatList(uid: String, page: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getChat(uid, page).let {
                if (it.body() != null) {
                    getChatLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun chatStudentHistory(uid: String, receiverId: String, page: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getChatStudentHistory(uid, receiverId, page).let {
                if (it.body() != null) {
                    chatHistoryLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun sendMessage(uid: String, receiverId: String, comments: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.sendMessage(uid, receiverId, comments).let {
                if (it.body() != null) {
                    sendMessageLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun getFreelancerJobs(uid: String, search: String, page: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getFeelancerJobs(uid, search, page).let {
                if (it.body() != null) {
                    freelancerJobsLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun getFreelancerStatus() {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getFreelancerStatus().let {
                if (it.body() != null) {
                    freelancerStatusLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun saveFreelancerDetails(request:SaveFreelancerRequest) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.saveFreelancerDetails(request).let {
                if (it.body() != null) {
                    saveFreelancerLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun updateFreelancerJobStatus(uid:String,id:String,status:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.updateJobStatus(uid,id,status).let {
                if (it.body() != null) {
                    freelancerUpdateStatusLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }
}