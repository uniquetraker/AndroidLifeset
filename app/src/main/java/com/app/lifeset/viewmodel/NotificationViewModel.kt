package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.model.DeleteNotification
import com.app.lifeset.model.NotificationResponse
import com.app.lifeset.repositories.NotificationRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(val notificationRepositories: NotificationRepositories) :
    ViewModel() {

    val isLoading = MutableLiveData(false)
    val isNotificationLiveData = MutableLiveData<NotificationResponse>()
    val isDeleteNotificationLiveData = MutableLiveData<DeleteNotification>()

    fun getNotifications(mobile: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            notificationRepositories.getNotifications(mobile).let {
                if (it.body() != null) {
                    isNotificationLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun deleteNotifications(id:String,mobile: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            notificationRepositories.deleteNotifications(id,mobile).let {
                if (it.body() != null) {
                    isDeleteNotificationLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

}