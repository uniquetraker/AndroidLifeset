package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.repositories.ForgotPasswordRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(val forgotPasswordRepositories: ForgotPasswordRepositories):ViewModel() {

    val isLoading = MutableLiveData(false)
    val forgotPasswordLiveData = MutableLiveData<BaseResponse>()

    fun doForgotPassword(mobileNumber: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            forgotPasswordRepositories.doForgotPassword(mobileNumber).let {
                if (it.body() != null) {
                    forgotPasswordLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }
}