package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.model.PersonalityDataResponse
import com.app.lifeset.model.PersonalityRequest
import com.app.lifeset.model.PersonalityResponse
import com.app.lifeset.repositories.PersonalityRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalityViewModel @Inject constructor(val repositories: PersonalityRepositories)
    :ViewModel(){

    val isLoading = MutableLiveData(false)
    val personalityLiveData = MutableLiveData<PersonalityResponse>()
    val personalityAnswerLiveData = MutableLiveData<PersonalityDataResponse>()

    fun getPersonalityData(uid:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getPersonalityData(uid).let {
                if (it.body() != null) {
                    personalityLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun doPersonalityAnswerData(request:PersonalityRequest) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.doPersonalityDataResponse(request).let {
                if (it.body() != null) {
                    personalityAnswerLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


}