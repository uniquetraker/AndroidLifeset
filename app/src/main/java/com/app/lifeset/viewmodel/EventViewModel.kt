package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.EventResponse
import com.app.lifeset.model.ExamResponse
import com.app.lifeset.repositories.EventRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(val repositories: EventRepositories)
    :ViewModel(){

    val isLoading = MutableLiveData(false)
    val eventLiveData = MutableLiveData<EventResponse>()

    fun getEventData() {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getEventData().let {
                if (it.body() != null) {
                    eventLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

}