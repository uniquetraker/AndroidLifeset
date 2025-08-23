package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.model.JobDetailModel
import com.app.lifeset.model.JobDetailResponse
import com.app.lifeset.model.JobResponse
import com.app.lifeset.model.LoginResponse
import com.app.lifeset.model.SettingsDateResponse
import com.app.lifeset.repositories.JobRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(val jobRepositories: JobRepositories) : ViewModel() {

    val isLoading = MutableLiveData(false)
    val jobLiveData = MutableLiveData<JobResponse>()
    val applyjobListLiveData = MutableLiveData<JobResponse>()

    val applyJobLiveData = MutableLiveData<BaseResponse>()
    val bookMarkLiveData = MutableLiveData<BaseResponse>()

    val jobDetailLiveData = MutableLiveData<JobDetailResponse>()
    val settingsMobileDateResponse=MutableLiveData<SettingsDateResponse>()

    fun getJobList(jbType: String, uid: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            jobRepositories.getJobList(jbType, uid).let {
                if (it.body() != null) {
                    jobLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }



    fun getSettingsMobileDateResponse( uid: String) {
        viewModelScope.launch {
            jobRepositories.getSettingsMobileDateResponse(uid).let {
                if (it.body() != null) {
                    settingsMobileDateResponse.postValue(it.body())
                } else {
                }
            }
        }
    }

    fun getJobDetail(uid: String, id: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            jobRepositories.getJobDetail(uid, id).let {
                if (it.body() != null) {
                    jobDetailLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun applyJob(collageId: String, uid: String, postId: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            jobRepositories.applyPost(collageId, uid, postId).let {
                if (it.body() != null) {
                    applyJobLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun addBookMark(collageId: String, uid: String, postId: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            jobRepositories.addBookMark(collageId, uid, postId).let {
                if (it.body() != null) {
                    bookMarkLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun getApplyJob(collageId: String, uid: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            jobRepositories.getAppliedJobs(collageId, uid).let {
                if (it.body() != null) {
                    applyjobListLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


}