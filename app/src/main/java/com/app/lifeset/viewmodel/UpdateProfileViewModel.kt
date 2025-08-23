package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.model.EducationInformationRequest
import com.app.lifeset.model.HomeTownRequest
import com.app.lifeset.model.PersonalInformationModel
import com.app.lifeset.model.SkillsRequest
import com.app.lifeset.model.UpdateProfileModel
import com.app.lifeset.repositories.UpdateProfileRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(val updateProfileRepositories: UpdateProfileRepositories) :
    ViewModel() {
    val isLoading = MutableLiveData(false)
    val updateProfileLiveData = MutableLiveData<UpdateProfileModel>()
    val personalInformationLiveData = MutableLiveData<UpdateProfileModel>()
    val educationInformationLiveData = MutableLiveData<UpdateProfileModel>()
    val updateHomeTownLiveData = MutableLiveData<UpdateProfileModel>()

    fun getUpdateSkills(request: SkillsRequest) {
        viewModelScope.launch {
            isLoading.postValue(true)
            updateProfileRepositories.getUpdateProfile(request).let {
                if (it.body() != null) {
                    updateProfileLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun getPersonalInformation(request: PersonalInformationModel) {
        viewModelScope.launch {
            isLoading.postValue(true)
            updateProfileRepositories.getPersonalInformation(request).let {
                if (it.body() != null) {
                    personalInformationLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun getEducationInformation(request: EducationInformationRequest) {
        viewModelScope.launch {
            isLoading.postValue(true)
            updateProfileRepositories.getEducationInformation(request).let {
                if (it.body() != null) {
                    educationInformationLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun updateHomeTownRequest(request: HomeTownRequest) {
        viewModelScope.launch {
            isLoading.postValue(true)
            updateProfileRepositories.updateHomeTown(request).let {
                if (it.body() != null) {
                    updateHomeTownLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


}