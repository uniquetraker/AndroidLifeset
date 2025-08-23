package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.model.CMSPageResponse
import com.app.lifeset.model.CasteResponse
import com.app.lifeset.model.CollageNameResponse
import com.app.lifeset.model.ProfileModel
import com.app.lifeset.model.StudentProfileModel
import com.app.lifeset.model.StudentProfileResponse
import com.app.lifeset.repositories.ProfileRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val profileRepositories: ProfileRepositories) :
    ViewModel() {

    val isLoading = MutableLiveData(false)
    val profileLiveData = MutableLiveData<ProfileModel>()
    val studentProfileLiveData = MutableLiveData<StudentProfileResponse>()
    val collageNameLiveData = MutableLiveData<CollageNameResponse>()
    val casteLiveData = MutableLiveData<CasteResponse>()

    val updateProfileLiveData = MutableLiveData<BaseResponse>()

    val updateProfile = MutableLiveData<ProfileModel>()

    val cmsPageLiveData = MutableLiveData<CMSPageResponse>()

    fun getProfile(uid: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            profileRepositories.getProfile(uid).let {
                if (it.body() != null) {
                    profileLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun getStudentProfile(uid: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            profileRepositories.getProfileDetail(uid).let {
                if (it.body() != null) {
                    studentProfileLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun getCollageName(id: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            profileRepositories.getCollageName(id).let {
                if (it.body() != null) {
                    collageNameLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }

    }

    fun getCastes(uid: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            profileRepositories.getCasts(uid).let {
                if (it.body() != null) {
                    casteLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun updateProfileApi(uid: RequestBody, profilePath: MultipartBody.Part?) {
        viewModelScope.launch {
            isLoading.postValue(true)
            profileRepositories.updateProfilePic(uid, profilePath).let {
                if (it.body() != null) {
                    updateProfile.postValue(it.body() as ProfileModel)
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun getCMSPage(page: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            profileRepositories.getPageDescription(page).let {
                if (it.body() != null) {
                    cmsPageLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun doUpdateProfile(
        uid: String,
        name: String,
        mobile: String,
        email: String,
        state: String,
        city: String,
        pincode: String,
        address: String,
        gender: String,
        religion: String,
        category: String,
        year: String,
        profile: String,
        langauge: String
    ) {
        viewModelScope.launch {
            isLoading.postValue(true)
            profileRepositories.doUpdateProfile(
                uid,
                name,
                mobile,
                email,
                state,
                city,
                pincode,
                address,
                gender,
                religion,
                category,
                year,
                profile,
                langauge
            ).let {
                if (it.body() != null) {
                    updateProfileLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


}