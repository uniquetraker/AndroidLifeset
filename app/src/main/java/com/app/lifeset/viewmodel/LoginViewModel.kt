package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.LoginResponse
import com.app.lifeset.repositories.LoginRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val loginRepositories: LoginRepositories) : ViewModel() {
    val isLoading = MutableLiveData(false)
    val loginLiveData = MutableLiveData<LoginResponse>()

    fun doLogin(mobileNumber: String, password: String,fcmToken:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            loginRepositories.doLogin(mobileNumber, password,fcmToken).let {
                if (it.body() != null) {
                    loginLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }

        }
    }
}