package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.repositories.ChangePasswordRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(val changePasswordRepositories: ChangePasswordRepositories) :
    ViewModel() {

    val isLoading = MutableLiveData(false)
    val changePasswordLiveData = MutableLiveData<BaseResponse>()

    fun doChangePassword(uid:String,cnfPassword: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            changePasswordRepositories.doChangePassword(uid,cnfPassword).let {
                if (it.body() != null) {
                    changePasswordLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }
}