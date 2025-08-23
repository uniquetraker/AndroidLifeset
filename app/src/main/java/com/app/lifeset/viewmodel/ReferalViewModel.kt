package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.AddReferalRequest
import com.app.lifeset.model.AddReferalResponse
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.model.DeleteReferalResponse
import com.app.lifeset.model.ReferalResponse
import com.app.lifeset.repositories.ReferalRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReferalViewModel @Inject constructor(val repositories: ReferalRepositories) :ViewModel(){

    val isLoading = MutableLiveData(false)
    val referalLiveData = MutableLiveData<ReferalResponse>()
    val addReferalLiveData = MutableLiveData<AddReferalResponse>()
    val deleteReferalLiveData = MutableLiveData<DeleteReferalResponse>()

    fun getReferalList(stuId:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getReferalList(stuId).let {
                if (it.body() != null) {
                    referalLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun addReferal(request:AddReferalRequest) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.addReferal(request).let {
                if (it.body() != null) {
                    addReferalLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun deleteReferal(stdId:String,id:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.deleteReferal(stdId,id).let {
                if (it.body() != null) {
                    deleteReferalLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }
}