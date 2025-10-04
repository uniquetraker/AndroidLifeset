package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.model.ExamDetailRequest
import com.app.lifeset.model.ExamDetailResponse
import com.app.lifeset.model.ExamResponse
import com.app.lifeset.repositories.ExamRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(val repositories: ExamRepositories) : ViewModel() {
    val isLoading = MutableLiveData(false)
    val examLiveData = MutableLiveData<ExamResponse>()
    val postInterestedLiveData = MutableLiveData<BaseResponse>()
    val examDetailLiveData = MutableLiveData<ExamDetailResponse>()

    fun getExamData(uid:String,langu:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getExam(uid,langu).let {
                if (it.body() != null) {
                    examLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }



    fun postInterested(post_id:String,uid:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.postInterested(post_id,uid).let {
                if (it.body() != null) {
                    postInterestedLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun getExamDetails(uid:String,post_id: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getExamDetails(uid,post_id).let {
                if (it.body() != null) {
                    examDetailLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }
}