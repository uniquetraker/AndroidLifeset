package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.CategoryResponse
import com.app.lifeset.model.ChangeCollageResponse
import com.app.lifeset.model.CollageResponse
import com.app.lifeset.model.CourseResponse
import com.app.lifeset.model.LoginResponse
import com.app.lifeset.model.OtpResponse
import com.app.lifeset.model.RegisterResponse
import com.app.lifeset.model.UpdateUserResponse
import com.app.lifeset.repositories.SignUpRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val signUpRepositories: SignUpRepositories) :
    ViewModel() {
    val isLoading = MutableLiveData(false)
    val changeCollageLiveData = MutableLiveData<ChangeCollageResponse>()
    val collageLiveData = MutableLiveData<CollageResponse>()
    val isUpdateUserLiveData = MutableLiveData<UpdateUserResponse>()

    val isCategoryLiveData = MutableLiveData<CategoryResponse>()

    val yearLiveData = MutableLiveData<CourseResponse>()

    val signUpLiveData = MutableLiveData<RegisterResponse>()
    val verifySignUpLiveData = MutableLiveData<OtpResponse>()

    fun getCollage() {
        viewModelScope.launch {
            isLoading.postValue(true)
            signUpRepositories.getCollage().let {
                if (it.body() != null) {
                    collageLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun updateUserData(
        mobile: String,
        collageId: String,
        newCollage: String,
        stream: String,
        courseId: String,
        year: String,
        collageName:String
    ) {
        viewModelScope.launch {
            isLoading.postValue(true)
            signUpRepositories.updateUserData(
                mobile, collageId, newCollage, stream,
                courseId, year,collageName
            ).let {
                if (it.body() != null) {
                    isUpdateUserLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun getChangeCollage(collageId: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            signUpRepositories.getChangeCollage(collageId).let {
                if (it.body() != null) {
                    changeCollageLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun getCategory(collageId: String, stream: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            signUpRepositories.getCategory(collageId, stream).let {
                if (it.body() != null) {
                    isCategoryLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun getYear(courseId: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            signUpRepositories.getYear(courseId).let {
                if (it.body() != null) {
                    yearLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun doRegister(
        email: String, mobile: String,
        name: String,
    ) {
        viewModelScope.launch {
            isLoading.postValue(true)
            signUpRepositories.doRegister(
                email, mobile, name
            ).let {
                if (it.body() != null) {
                    signUpLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }

    }


    fun doVerifyRegister(
        otp: String,
        email: String, mobile: String,
        name: String,
        token: String,
        fcmToken:String
    ) {
        viewModelScope.launch {
            isLoading.postValue(true)
            signUpRepositories.doVerifyRegister(
                otp, email, mobile, name, token,fcmToken
            ).let {
                if (it.body() != null) {
                    verifySignUpLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }

    }


}